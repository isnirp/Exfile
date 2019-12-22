package com.flimbis.exfile.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.flimbis.exfile.R

class CreateFolderDialog(val type: Int) : DialogFragment() {
    private var listener: OnCreateDialogClickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val layoutInflater = it.layoutInflater
            val view = layoutInflater.inflate(R.layout.dialog_create_folder, null)

            builder.setView(view)
            if (type == 0) builder.setTitle("Create Folder") else builder.setTitle("Create File")

            val name = view.findViewById<EditText>(R.id.edt_folder_name)
            val createFolder = view.findViewById<Button>(R.id.bttn_create_folder)
            createFolder.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    when{
                        type == 0 -> listener!!.onCreateFolder(name.text.toString())
                        type == 1 -> listener!!.onCreateFile(name.text.toString())
                    }

                }
            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCreateDialogClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCreateDialogClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCreateDialogClickListener {
        fun onCreateFolder(folderName: String)

        fun onCreateFile(folderName: String)
    }
}