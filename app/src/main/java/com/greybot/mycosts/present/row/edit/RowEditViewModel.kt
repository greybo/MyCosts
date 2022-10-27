package com.greybot.mycosts.present.row.edit

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.dto.RowDto
import com.greybot.mycosts.data.repository.row.RowDataSource

class RowEditViewModel : CompositeViewModel() {

    private var model: RowDto? = null
    private val dataSource = RowDataSource()
    val status = MutableLiveData<RowDto?>()
    fun fetchData(objectId: String?) {
        objectId ?: throw Throwable()
        launchOnDefault {
            model = dataSource.findById(objectId)
            makeItems(model)
        }
    }

    private fun makeItems(model: RowDto?) {
        status.postValue(model)
    }

//    fun editRow(editModel: RowDto?) {
//        dataSource.save(editModel)
//    }

    fun saveAndExit(name: Editable, count: Editable?, price: Editable?) {
        val _price = try {
            price.toString().toFloat()
        } catch (e: NumberFormatException) {
            0f
        }
        val _count = try {
            count.toString().toFloat()
        } catch (e: NumberFormatException) {
            1F
        }
        val editModel = model?.copy(nameRow = name.toString(), count = _count, price = _price)
        dataSource.save(editModel)
    }
}

