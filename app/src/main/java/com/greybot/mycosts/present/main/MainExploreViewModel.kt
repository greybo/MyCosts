package com.greybot.mycosts.present.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.greybot.mycosts.base.CompositeViewModel
import com.greybot.mycosts.data.repository.ExploreRepository
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.present.second.ExploreHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainExploreViewModel @Inject constructor(private val exploreRepo: ExploreRepository) :
    CompositeViewModel() {

    private val exploreHandler by lazy { ExploreHandler() }

    private var _state = MutableLiveData<List<AdapterItems>>()
    val state: LiveData<List<AdapterItems>> = _state

    fun fetchData() {
        launchOnDefault {
            val folders = exploreRepo.getAll()
            val items = exploreHandler.makeFolderItems(folders)
            _state.postValue(items)
        }
    }
}
