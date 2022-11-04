package com.greybot.mycosts.present.row.add

import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.dto.CurrencyDto
import com.greybot.mycosts.data.dto.ExploreRow
import com.greybot.mycosts.data.repository.explore.ExploreDataSource
import com.greybot.mycosts.data.repository.row.FileDataSource
import com.greybot.mycosts.utility.LogApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RowAddViewModel @Inject constructor(
    private val fileSource: FileDataSource,
    private val exploreSource: ExploreDataSource
) : CompositeViewModel() {

    private var exploreRow: ExploreRow? = null

    fun fetchData(objectId: String) {
        exploreRow = exploreSource.findParent(objectId)
    }

    fun addRow(
        path: String,
        rowName: String,
        count: String = "1",
        price: Float = 0F,
        currency: CurrencyDto? = null,
        parentId: String?
    ) {
        var _count = try {
            count.toInt()
        } catch (e: Exception) {
            LogApp.w("addRow_tag", e)
            1
        }
        if (_count == 0) {
            _count = 1
        }
        fileSource.addRow(path, rowName, _count, price, currency, parentId)
        exploreRow?.let {
            exploreSource.updateFolder(it.copy(isFiles = true))
        } ?: throw Throwable()
    }
}