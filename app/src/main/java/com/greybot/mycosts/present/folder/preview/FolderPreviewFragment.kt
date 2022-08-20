package com.greybot.mycosts.present.folder.preview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import au.com.crownresorts.crma.extensions.setGoneOrVisible
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.databinding.FolderPreviewFragmentBinding
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.present.explore.ExploreAdapter
import com.greybot.mycosts.utility.getRouter

class FolderPreviewFragment :
    BaseBindingFragment<FolderPreviewFragmentBinding>(FolderPreviewFragmentBinding::inflate) {

    private val viewModel by viewModels<FolderPreviewViewModel>()
    private var adapter: ExploreAdapter? = null
    private var args: FolderPreviewFragmentArgs? = null
    private val router: IFolderPreviewRouter by getRouter()
    private var buttonType: ButtonType = ButtonType.None

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            args = FolderPreviewFragmentArgs.fromBundle(it)
        }
//        setFragmentResultListener(FRAGMENT_RESULT_ADD_FOLDER) { key, bundle ->
//            if (FRAGMENT_RESULT_ADD_FOLDER == key) {
//                val name = (bundle.get(ARG_FOLDER_NAME) as? String)
//                val path = (bundle.get(ARG_FOLDER_PATH) as? String)
//                viewModel.addFolder(name, path)
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.state.observe(viewLifecycleOwner) {
            adapter?.updateAdapter(it)
        }
        viewModel.stateButton2.observe2(viewLifecycleOwner) {
            buttonType = it
            binding.folderPreviewFloatButton.setGoneOrVisible(it != ButtonType.None)
        }
        viewModel.fetchData(args?.pathName)
    }

    private fun initViews() {
        with(binding) {
            folderPreviewFloatButton.setOnClickListener {
                handleButtonClick(buttonType)
            }

            adapter = ExploreAdapter {
                handleAdapterClick(it)
            }
            folderPreviewRecyclerView.setHasFixedSize(true)
            folderPreviewRecyclerView.adapter = adapter
        }
    }

    private fun handleAdapterClick(item: AdapterItems) {
        when (item) {
            is AdapterItems.ButtonAddItem -> handleButtonClick(item.type)
            is AdapterItems.FolderItem -> router.fromFolderToFolder(item.path)
            is AdapterItems.RowItem -> router.fromFolderToEditRow(item.objectId)
        }
    }

    private fun handleButtonClick(type: ButtonType, path: String = args?.pathName ?: "") {
        when (type) {
            ButtonType.Folder -> router.fromFolderToAddFolder(path)
            ButtonType.Row -> router.fromFolderToAddRow(path)
            else -> {}
        }
    }
}