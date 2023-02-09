package com.greybot.mycosts.present.folder.preview

import androidx.lifecycle.SavedStateHandle
import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.dto.ExploreRow
import com.greybot.mycosts.data.dto.FileRow
import com.greybot.mycosts.data.repository.explore.FolderDataSource
import com.greybot.mycosts.data.repository.row.FileDataSource
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.present.file.FileHandler
import com.greybot.mycosts.present.folder.FolderHandler
import com.greybot.mycosts.utility.MyLiveData
import com.greybot.mycosts.utility.makeLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exploreSource: FolderDataSource,
    private val rowSource: FileDataSource
) : CompositeViewModel() {

    private val fileHandler by lazy { FileHandler() }
    private val listDelete = mutableListOf<String>()
    private val deleteIconLiveData = makeLiveData<Boolean>()

    val state = makeLiveData<List<AdapterItems>>()
    val title = makeLiveData<String?>()
    val parentId by lazy { savedStateHandle.get<String>("objectId") ?: "" }

    init {
        launchOnDefault {
            val parentFolder = exploreSource.findByObjectId(parentId)
            title.postValue(parentFolder?.name)
        }
    }

    fun fetchData(id: String = parentId) {
        launchOnDefault {
            val folderList = exploreSource.getListById(id)
            val files = rowSource.getListById(id)

            if (!folderList.isNullOrEmpty()) {
                makeFolderList(folderList)
            } else if (files.isNotEmpty()) {
                makeFileList(files)
            } else makeButtonList()
        }
    }

    private suspend fun makeFolderList(list: List<ExploreRow>) {
        val folderHandler = FolderHandler(exploreSource.fetchData(), rowSource.fetchData())
        setToLiveData = folderHandler.makeFolderItems(list)
    }

    private fun makeFileList(rowList: List<FileRow>) {
        setToLiveData = fileHandler.makeGroupBuy(rowList)
    }

    fun changeRowBuy(item: AdapterItems.RowItem) {
        launchOnDefault {
            rowSource.changeBuyStatus(item.objectId)
            updateUIRowList()
        }
    }

    private suspend fun updateUIRowList() {
//        val files = rowSource.getCurrentList(parentId)
//        makeFileList(files)
        fetchData()
    }

    fun changeRowPrice(id: String, count: Int, price: Float) {
        launchOnDefault {
            rowSource.changePrice(id, count, price)
            updateUIRowList()
        }
    }

    private fun makeButtonList() {
        setToLiveData = listOf(
            AdapterItems.ButtonAddItem(ButtonType.Folder),
            AdapterItems.ButtonAddItem(ButtonType.Row)
        )
    }

    fun deleteIconLiveData(): MyLiveData<Boolean> {
        deleteIconLiveData.values = false
        listDelete.clear()
        return deleteIconLiveData
    }

    fun fileHighlight(objectId: String) {
        if (listDelete.contains(objectId)) {
            listDelete.remove(objectId)
        } else {
            listDelete.add(objectId)
        }
        deleteIconLiveData.values = listDelete.isNotEmpty()
    }

    fun deleteSelectItems() {
        launchOnDefault {
            listDelete.map { objectId ->
                rowSource.delete(objectId)
            }
            listDelete.clear()
            fetchData()
        }
        deleteIconLiveData.values = false
    }

    private var setToLiveData: List<AdapterItems>
        get() = state.values
        set(value) {
            state.postValue(value)
        }
}

enum class ButtonType(val row: String) {
    Folder("Папка"), Row("Товар"), None("")
}