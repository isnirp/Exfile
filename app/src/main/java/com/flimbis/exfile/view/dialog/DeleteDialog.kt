package com.flimbis.exfile.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.adapter.DeleteFilesAdapter

class DeleteDialog(val items: List<FileModel>) : DialogFragment() {
    private lateinit var adapter: DeleteFilesAdapter
    private var listener: OnDeleteDialogClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val layoutInflater = it.layoutInflater
            val view = layoutInflater.inflate(R.layout.dialog_delete, null)

            builder.setView(view)

            val deleteAction = view.findViewById<Button>(R.id.bttn_delete_files)
            val chkBox = view.findViewById<CheckBox>(R.id.chk_delete)
            val lst = view.findViewById<ListView>(R.id.lst_delete_files)
            adapter = DeleteFilesAdapter(items)
            lst.adapter = adapter

            chkBox.setOnClickListener { deleteAction.isEnabled = true }

            deleteAction.setOnClickListener {
                listener!!.onDeleteFile(items)

                dialog.cancel()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDeleteDialogClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnDeleteDialogClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDeleteDialogClickListener {
        fun onDeleteFile(files: List<FileModel>)
    }
}