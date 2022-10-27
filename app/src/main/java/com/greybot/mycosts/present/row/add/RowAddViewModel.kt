package com.greybot.mycosts.present.row.add

import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.dto.CurrencyDto
import com.greybot.mycosts.data.repository.row.RowDataSource
import com.greybot.mycosts.utility.LogApp
import com.greybot.mycosts.views.AppCoordinator

class RowAddViewModel : CompositeViewModel() {

    private val source: RowDataSource get() = AppCoordinator.shared.rowDataSource

    fun addRow(path: String, rowName: String, count: Float = 1f, price: Float = 0F, currency: CurrencyDto? = null, parentId: String?) {
        var _count = try {
            count
        } catch (e: Exception) {
            LogApp.w("addRow_tag", e)
            1F
        }
        if (_count == 0f) {
            _count = 1f
        }
        source.addRow(path, rowName, _count, price, currency, parentId)
    }
}