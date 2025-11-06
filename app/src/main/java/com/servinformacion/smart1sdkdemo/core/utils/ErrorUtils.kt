package com.servinformacion.smart1sdkdemo.core.utils

import com.servinformacion.smart1sdk.android.core.error.ApiError
import com.servinformacion.smart1sdk.android.core.error.ApiListError
import com.servinformacion.smart1sdk.android.core.error.CommonError
import com.servinformacion.smart1sdk.android.core.error.ErrorS1SDK
import com.servinformacion.smart1sdk.android.core.error.SdkInitConfigError

object ErrorUtils {
    /**
     * This function returns general and ambiguous string message based on the error type.
     *
     * Note: We highly recommend to modify this function to return more specific messages based on the
     * error type and the context where this error comes from. Or even create an specific function
     * for each context where some error could be returned.
     *
     * Example:
     * Is no the same getting an [ApiError.NotFound] when
     * - Requesting some data (like orders, ports, etc) - In this case it means that the requested
     * data was not found.
     * - Requesting an update of the state of an order - In this case it means that the order that
     * we are trying to update does not exist or wasn't found by the api.
     *
     * So, based on this example, we remind you that it is important to understand that
     * the same error type can have different meanings depending on the context where it comes from.
     * */
    fun toMsg(
        error : ErrorS1SDK,
    ): String {
        return when (error) {
            is SdkInitConfigError, is CommonError, is ApiError -> {
                mappingSingleErrorMessages(error)
            }
            is ApiListError -> {
                when (error) {
                    is ApiListError.AllErrors<*> -> {
                        val errorMessages: MutableList<String> = mutableListOf()
                        error.cause.forEach {
                            errorMessages.add(
                                "${mappingSingleErrorMessages(it.second)} [${it.first}]"
                            )
                        }
                        """
                            (Api List Error)
                            
                            ${errorMessages.joinToString("\n - ")}
                        """.trimIndent()
                    }
                }
            }
            else -> "Unknown error"
        }
    }

    private fun mappingSingleErrorMessages(
        error : ErrorS1SDK,
    ): String {
        return when (error) {
            is SdkInitConfigError -> {
                when (error) {
                    SdkInitConfigError.NotFound          -> "There is no SDK configuration found"
                    SdkInitConfigError.SdkApiKeyNotFound -> "There is no SDK API key found"
                    is SdkInitConfigError.Unknown        -> "Unknown error when initializing SDK - ${error.cause}"
                }
            }
            is CommonError -> {
                when (error) {
                    is CommonError.InvalidInputData     -> "The input data is invalid (${error.cause}) (Common Error)"
                    is CommonError.InvalidInputDataList -> "The input data list is invalid (${error.cause}) (Common Error)"
                    CommonError.NotFound                -> "No data found (Common Error)"
                    is CommonError.Unknown              -> "Unknown Common Error - ${error.cause}"
                }
            }
            is ApiError -> {
                when (error) {
                    ApiError.NotInternet              -> "The device is not connected to the internet (Api Error)"
                    ApiError.Timeout                  -> "The request timed out (Api Error)"
                    ApiError.ExpiredToken             -> "The token has expired (Api Error)"
                    ApiError.NotFound                 -> "The requested data or resource was not found (Api Error)"
                    ApiError.Unauthorized             -> "The user is not authorized to make this request (Api Error)"
                    is ApiError.MissingDataOnResponse -> "Missing data on response - (${error.message}) (Api Error)"
                    ApiError.Serialization            -> "Serialization Api Error when parsing response"
                    is ApiError.ServerError           -> "Server Api Error - Details \n\n  - code: ${error.code}\n\n  - message: ${error.message}\n\n  - messages: ${error.messages}"
                    is ApiError.Unknown               -> "Unknown Api Call Error - ${error.cause}"
                }
            }
            else -> "Unknown error"
        }
    }
}