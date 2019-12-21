package com.flimbis.exfile.view.home


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.Toast

import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.SimpleDividerItemDecoration
import com.flimbis.exfile.view.adapter.ExFileAdapter
import com.flimbis.exfile.view.adapter.FileAdapter
import java.io.File
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.widget.PopupMenu
import android.R.attr.name
import com.flimbis.exfile.util.getFilesFromPath


/**
 * A simple [Fragment] subclass.
 *major lifecycle methods of a fragment to implement; onCreate, onCreateView, onPause
 */
class ExFilesFragment : androidx.fragment.app.Fragment(), ExFileAdapter.OnFileClickedListener {

    lateinit var listFiles: ListView
    lateinit var adapter: ExFileAdapter

    private var listener: OnFileSelectedListener? = null

    companion object {
        private const val PATH = "pathFinder"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    /*
    * initialize essential components of the fragment that you want to retain
    * when the fragment is paused or stopped, then resumed.
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_files_ex, container, false)

        setHasOptionsMenu(true)

        listFiles = view.findViewById(R.id.lst_ex_files)

        adapter = ExFileAdapter(this, context, getFileModelList(arguments!!.getString(PATH)))
        adapter.setFileClickedListener(this)
        listFiles.adapter = adapter

        return view
    }

    /*
    * commit any changes that should be persisted beyond the current user session (because the user might not come back)
    * */
    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                Toast.makeText(context,"search", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_new -> {
                Toast.makeText(context,"new", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_view -> {
                listener!!.onItemViewSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFileSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onItemClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onFileClicked(fileModel: FileModel) {
        listener!!.onItemFileSelected(fileModel)
    }

    override fun onPopMenuClicked(v: View,fileModel: FileModel) {
        val popup = PopupMenu(context!!, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_pop, popup.menu)

        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.menu_pop_rename -> {
                        //Todo a dialog that allows file rename
                        popup.dismiss()
                        true
                    }
                    R.id.menu_pop_property -> {
                        listener?.onItemPropertySelected(fileModel)
                        popup.dismiss()
                        true
                    }
                    else -> false
                }
            }
        })
        popup.show()
    }

    fun initMultiChoiceMode(listener: AbsListView.MultiChoiceModeListener){
        listFiles.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listFiles.setMultiChoiceModeListener(listener)
    }

    fun actionModeActivated(fileModel: FileModel){
        listener!!.onActionModeActivated(fileModel)
    }

    private fun showMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getFileModelList(path: String): List<FileModel> {
        var files: List<File> = getFilesFromPath(path)
        return files.map { FileModel(path = it.path, isDirectory = it.isDirectory, name = it.name, size = it.length(), ext = it.extension, lastModified = it.lastModified()) }
    }

    interface OnFileSelectedListener {
        fun onItemFileSelected(fileModel: FileModel)

        fun onItemPropertySelected(fileModel: FileModel)

        fun onItemViewSelected()

        fun onActionModeActivated(fileModel: FileModel)
    }


    class Builder {
        var path: String = ""

        fun build(): ExFilesFragment {
            val fragment = ExFilesFragment()
            val args = Bundle()
            args.putString(PATH, path)
            fragment.arguments = args
            return fragment
        }
    }

}
