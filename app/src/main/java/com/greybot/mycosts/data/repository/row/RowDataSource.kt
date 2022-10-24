package com.greybot.mycosts.data.repository.row

import com.greybot.mycosts.data.dto.CurrencyDto
import com.greybot.mycosts.data.dto.RowDto

class RowDataSource {
    private val repo: RowRepo = RowRepo()

    fun geBackupList() = repo.backupList

    suspend fun findByPath(
        findPath: String
    ) = repo.getAll().filter { it.path.startsWith(findPath) }


    suspend fun findById(
        objectId: String
    ) = repo.getAll().find { dto -> dto.objectId == objectId }

    fun addRow(
        path: String,
        rowName: String,
        count: Int = 0,
        price: Float = 0F,
        currency: CurrencyDto? = null,
        parentId: String?
    ) {
        val row = RowDto(
            path = path,
            title = rowName,
            count = count,
            price = price,
            currency = currency,
            parentId = parentId
        )
        repo.addRow(row)
    }

    fun changeBuyStatus(objectId: String) {
        val list = geBackupList().map { row ->
            if (row.objectId == objectId) {
                val changedRow = row.copy(bought = !row.bought)
                repo.saveModel(changedRow)
                changedRow
            } else row
        }
        repo.saveBackupList(list)
    }

    fun save(model: RowDto?) {
        model?.let { repo.saveModel(it) }
    }
}
