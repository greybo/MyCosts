package com.greybot.mycosts.present.second.add

import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.dto.ExploreRow
import com.greybot.mycosts.data.repository.explore.ExploreDataSource
import com.greybot.mycosts.utility.myLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderEditViewModel @Inject constructor(private val source: ExploreDataSource) :
    CompositeViewModel() {

    val state = myLiveData<ExploreRow?>()

    fun fetchData(objectId: String) {
        launchOnDefault {
            val model = source.findParent(objectId)
            state.values = model
        }
    }

    fun updateFolderNew(name: String?) {
        if (name != null) {
            val explore = ExploreRow(name)
            source.update(explore)
        }
    }
}