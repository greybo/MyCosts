package com.greybot.mycosts.present.row.edit

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.data.dto.RowDto
import com.greybot.mycosts.databinding.RowAddFragmentBinding

class RowEditFragment :
    BaseBindingFragment<RowAddFragmentBinding>(RowAddFragmentBinding::inflate) {

    private val viewModel by viewModels<RowEditViewModel>()
    private val args by lazy { arguments?.let { RowEditFragmentArgs.fromBundle(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner) { rowDto ->
            rowDto?.let { initView(it) } ?: findNavController().popBackStack()
        }
        viewModel.fetchData(args?.objectId)
    }

    private fun initView(rowDto: RowDto) {
        with(binding) {
            addRowName.setText(rowDto.nameRow)
            addRowCount.setText(rowDto.count.toString())
            addRowPrice.setText(rowDto.price.toString())
            addRowPrice.setOnEditorActionListener { _, actionId, _ ->
               ( if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveAndExit()
                } else false).also { addRowName.requestFocus() }
            }
        }
    }

    private fun saveAndExit(): Boolean {
        with(binding) {
            if (!addRowName.text.isNullOrBlank()) {
                viewModel.saveAndExit(
                    name = addRowName.text!!,
                    count = addRowCount.text,
                    price = addRowPrice.text,
                )
                findNavController().popBackStack()
                return true
            }
        }
        return false
    }
}
