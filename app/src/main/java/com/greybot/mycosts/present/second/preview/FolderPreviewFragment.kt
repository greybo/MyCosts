package com.greybot.mycosts.present.second.preview

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greybot.mycosts.R
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.base.systemBackPressedCallback
import com.greybot.mycosts.databinding.FolderPreviewFragmentBinding
import com.greybot.mycosts.present.adapter.AdapterCallback
import com.greybot.mycosts.present.adapter.ExploreAdapter
import com.greybot.mycosts.utility.getRouter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderPreviewFragment :
    BaseBindingFragment<FolderPreviewFragmentBinding>(FolderPreviewFragmentBinding::inflate) {

    private val viewModel by viewModels<FolderPreviewViewModel>()
    private var adapter: ExploreAdapter? = null

    private val router: IFolderPreviewRouter by getRouter()
    private var buttonType: ButtonType = ButtonType.None

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        val toolbar = binding.folderPreviewToolbar.getBuilder()
            .homeCallback { backPress() }
            .create()
        viewModel.state.observe(viewLifecycleOwner) {
            adapter?.updateAdapter(it)
        }
        viewModel.title.observe(viewLifecycleOwner) {
            toolbar.title = it ?: ""
        }
        systemBackPressedCallback { backPress() }
        viewModel.fetchData()
    }

    private fun backPress() {
        findNavController().popBackStack()
    }

    private fun initViews() {
//        binding.folderPreviewFloatButton.setOnClickListener {
//            handleButtonClick(buttonType)
//        }
        initAdapter()
    }

    private fun initAdapter() {
        with(binding) {
            adapter = ExploreAdapter {
                handleAdapterClick(it)
            }
            folderPreviewRecyclerView.setHasFixedSize(true)
            folderPreviewRecyclerView.adapter = adapter
        }
    }

    private fun handleAdapterClick(callback: AdapterCallback) {
        with(callback) {
            when (this) {
                is AdapterCallback.RowName -> router.fromFolderToEditRow(
                    this.value.objectId
                )
                is AdapterCallback.RowPrice -> {showDialogOne()}
                is AdapterCallback.RowBuy -> viewModel.changeRowBuy(this.value)
                is AdapterCallback.AddButton -> handleButtonClick(this.value.type)
                is AdapterCallback.FolderOpen -> router.fromFolderToFolder(
                    this.value.objectId ?: ""
                )
                else -> {}
            }
        }
    }

    private fun handleButtonClick(
        type: ButtonType,
        id: String = viewModel.parentId
    ) {
        when (type) {
            ButtonType.Folder -> router.fromFolderToAddFolder(id)
            ButtonType.Row -> router.fromFolderToAddRow(id)
            else -> {}
        }
    }

    fun showDialogOne() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.sample_dialog_one)
        val btnEdit = dialog.findViewById<RelativeLayout>(R.id.rl_edit)
        val btnDelete = dialog.findViewById<RelativeLayout>(R.id.rl_delete)
        val btnAdd = dialog.findViewById<RelativeLayout>(R.id.rl_add)

        btnEdit?.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on Edit", Toast.LENGTH_SHORT).show()
        }
        btnDelete?.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on Delete", Toast.LENGTH_SHORT).show()
        }
        btnAdd?.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on Add", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }
}
