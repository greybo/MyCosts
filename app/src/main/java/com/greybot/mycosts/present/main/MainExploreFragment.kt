package com.greybot.mycosts.present.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import au.com.crownresorts.crma.extensions.gone
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.databinding.ExploreFragmentBinding
import com.greybot.mycosts.present.adapter.AdapterCallback
import com.greybot.mycosts.theme.MyCostsTheme
import com.greybot.mycosts.utility.ROOT_FOLDER
import com.greybot.mycosts.utility.getRouter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainExploreFragment :
    BaseBindingFragment<ExploreFragmentBinding>(ExploreFragmentBinding::inflate) {

    private val viewModel by viewModels<MainExploreViewModel>()
    private val router: IMainExploreRouter by getRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            MyCostsTheme {
                MainExampleScreen(viewModel, ::handleOnClick)
            }
        }
        initViews()
        binding.toolbar.getBuilder()
            .title("My costs")
            .create()
        binding.toolbar.gone()
        viewModel.fetchData()
    }

    private fun initViews() {
        with(binding) {
            exploreFloatButton.gone()
//            exploreFloatButton.setOnClickListener {
//                exploreFloatButton.animateFabHide {
//                    router.fromExploreToAddFolder("root")
//                }
//            }
        }
    }

    private fun handleOnClick(callback: AdapterCallback) {
        when (callback) {
            is AdapterCallback.FolderOpen -> {
//                binding.exploreFloatButton.animateFabHide {
                    router.fromExploreToFolder(callback.value.objectId ?: ROOT_FOLDER)
//                }
            }
            is AdapterCallback.FolderHighlight -> {
//                binding.exploreFloatButton.animateFabHide {
//                    router.fromExploreToFolder(, callback.value.path)
//                }
            }
            else -> TODO()
        }
    }

    override fun onResume() {
        super.onResume()
//        binding.exploreFloatButton.animateShowFab()
    }
}

