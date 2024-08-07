package com.greybot.mycosts.present.folder.add

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greybot.mycosts.base.BaseBindingFragment
import com.greybot.mycosts.data.dto.FolderRow
import com.greybot.mycosts.databinding.FolderAddFragmentBinding
import com.greybot.mycosts.utility.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FolderEditFragment :
    BaseBindingFragment<FolderAddFragmentBinding>(FolderAddFragmentBinding::inflate) {

    private val viewModel by viewModels<FolderEditViewModel>()
    private val args by lazy { arguments?.let { FolderAddFragmentArgs.fromBundle(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.state.observe(viewLifecycleOwner) {
            fillFields(it)
        }
    }

    private fun fillFields(row: FolderRow?) {
        with(binding) {
            addFolderName.setText(row?.name)
        }
    }

    private fun initViews() {
        with(binding) {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            addFolderDate.text = sdf.format(Date())
            addFolderName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Do whatever you want here
                        saveFolder(addFolderName.text.toString())
                        return true;
                    }
                    return false;
                }
            })
            addFolderName.requestFocus()
            showKeyboard()
        }
    }

    private fun saveFolder(name: String) {
        viewModel.updateFolderNew(name/*, args?.path, Date().time*/)
        findNavController().popBackStack()
    }
}