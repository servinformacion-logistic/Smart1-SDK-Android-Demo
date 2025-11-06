package com.servinformacion.smart1sdkdemo.core.utils

import com.servinformacion.smart1sdk.android.core.model.DataSet

object PaginationUtils {
    suspend fun <T> getPaginatedData(
        getData        : suspend (page: Int) -> DataSet<T>?,
        onProgress     : ( suspend (currentDownloadedRecords: Int, totalRecords: Int) -> Unit)? = null,
        onDataProgress : ( suspend (data: List<T>) -> Unit)? = null,
    ): List<T> {
        val allData = mutableListOf<T>()
        var countOfRecords = 0
        val firstData: DataSet<T> = getData(1) ?: return allData
        allData.addAll(firstData.records)
        countOfRecords += firstData.records.size
        onProgress?.invoke(countOfRecords, firstData.pagination.totalRecords)
        onDataProgress?.invoke(firstData.records)
        if (firstData.pagination.totalPages <= 1) {
            return allData
        }
        var currentPage = 2
        while (currentPage <= firstData.pagination.totalPages) {
            val somePageData: DataSet<T> = getData(currentPage) ?: break
            allData.addAll(somePageData.records)
            countOfRecords += somePageData.records.size
            onProgress?.invoke(countOfRecords, somePageData.pagination.totalRecords)
            onDataProgress?.invoke(somePageData.records)
            currentPage++
        }
        return allData
    }
}