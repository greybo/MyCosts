package com.greybot.mycosts.present.second.preview

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.base.systemBackPressedCallback
import com.greybot.mycosts.databinding.FolderPreviewFragmentBinding
import com.greybot.mycosts.databinding.SampleDialogOneBinding
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.present.adapter.AdapterCallback
import com.greybot.mycosts.present.adapter.ExploreAdapter
import com.greybot.mycosts.utility.LogApp
import com.greybot.mycosts.utility.bindingDialog
import com.greybot.mycosts.utility.getRouter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderPreviewFragment :
    BaseBindingFragment<FolderPreviewFragmentBinding>(FolderPreviewFragmentBinding::inflate) {
    private val log_tag = "FolderPreviewFragment"
    private val viewModel by viewModels<FolderPreviewViewModel>()
    private var adapter: ExploreAdapter? = null

    private val router: IFolderPreviewRouter by getRouter()
//    private var buttonType: ButtonType = ButtonType.None

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
                is AdapterCallback.RowPrice -> {
                    showDialogOne(this.value)
                }
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

    private fun showDialogOne(model: AdapterItems.RowItem) {
        val dialog = BottomSheetDialog(requireContext())
        val binding = bindingDialog(requireContext(), SampleDialogOneBinding::inflate)

        dialog.setContentView(binding.root)

        binding.bottomSheetEditCount.hint = "${model.count} шт"
        binding.bottomSheetEditPrice.hint = "${model.price} грн"

        binding.bottomSheetEditPrice.setOnEditorActionListener { _, editorInfo, _ ->
            if (editorInfo == EditorInfo.IME_ACTION_DONE) {
                binding.bottomSheetEditCount.text?.toString()
                val count = if (binding.bottomSheetEditCount.text.isNotEmpty()) {
                    binding.bottomSheetEditCount.text.toString().toInt()
                } else
                    model.count

                val price = if (binding.bottomSheetEditPrice.text.isNotEmpty()) {
                    binding.bottomSheetEditPrice.text.toString().toFloat()
                } else
                    model.price

//                val price = binding.bottomSheetEditPrice.text?.toString()?.toFloat() ?: model.price
                LogApp.i(log_tag, "$count | $price")

                viewModel.changeRowPrice(id = model.objectId, count = count, price = price)
                dialog.dismiss()
                true
            } else
                false
        }
        dialog.show()
    }

}
