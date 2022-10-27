package com.greybot.mycosts.present.row.add

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.databinding.RowAddFragmentBinding

class RowAddFragment : BaseBindingFragment<RowAddFragmentBinding>(RowAddFragmentBinding::inflate) {

    private val args by lazy { arguments?.let { RowAddFragmentArgs.fromBundle(it) } }
    private val viewModel by viewModels<RowAddViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        args?.path ?: throw Throwable()
        with(binding) {
            addRowPrice.setOnEditorActionListener { view, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveAndExit()
                    true
                } else false
            }
            addRowButton.setOnClickListener {
                saveAndExit()
            }
        }
    }

    private fun saveAndExit() {
        with(binding) {
            val name = addRowName.text.toString()
            val price = try {
                addRowPrice.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                0f
            }
            val count = try {
                addRowCount.text.toString().toInt()
            } catch (e: NumberFormatException) {
                1
            }
            if (name.isNotBlank()) {
                viewModel.addRow(
                    path = args!!.path,
                    rowName = name,
                    count = count,
                    price = price,
                    parentId = args?.objectId
                )
            }
            findNavController().popBackStack()
        }
    }
}