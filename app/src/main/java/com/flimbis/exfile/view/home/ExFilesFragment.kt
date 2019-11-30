package com.flimbis.exfile.view.home


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AbsListView
import android.widget.Toast

import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.util.SimpleDividerItemDecoration
import com.flimbis.exfile.view.adapter.FileAdapter
import java.io.File


/**
 * A simple [Fragment] subclass.
 *major lifecycle methods of a fragment to implement; onCreate, onCreateView, onPause
 */
class ExFilesFragment : androidx.fragment.app.Fragment(), FileAdapter.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    lateinit var listFiles: androidx.recyclerview.widget.RecyclerView
    lateinit var adapter: FileAdapter
    //var filesList = listOf<FileModel>()

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

        listFiles = view.findViewById(R.id.lst_home_files)
        listFiles.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(view.ctx)
        listFiles.addItemDecoration(SimpleDividerItemDecoration(context))

        adapter = FileAdapter(getFileModelList(arguments!!.getString(PATH)))
        adapter.registerItemClickListener(this)

        listFiles.adapter = adapter


        return view
    }

    /*
    * commit any changes that should be persisted beyond the current user session (because the user might not come back)
    * */
    override fun onPause() {
        super.onPause()
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

    override fun onItemClicked(fileModel: FileModel) {
        listener?.onFileSelected(fileModel)
    }

    private fun showMsg(msg: String) {
        //longToast(msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getFilesFromPath(path: String): List<File> {
        val file = File(path)
        return file.listFiles().toList()
    }

    private fun getFileModelList(path: String): List<FileModel> {
        var files: List<File> = getFilesFromPath(path)
        return files.map { FileModel(path = it.path, isDirectory = it.isDirectory, name = it.name, size = it.length(), ext = it.extension, lastModified = it.lastModified()) }
    }

    /*fun convertFileSizeToMB(sizeInBytes: Long): Double {
        return (sizeInBytes.toDouble()) / (1024 * 1024)
    }*/

    /*
    * contextual menu
    * */
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        // Respond to clicks on the actions in the CAB
        return when (item?.itemId) {

            R.id.menu_cut -> {
                mode?.finish()
                true
            }
            R.id.menu_copy -> {
                mode?.finish()
                true
            }
            R.id.menu_delete -> {
                //deleteSelectedItems()
                mode?.finish() // Action picked, so close the CAB
                true
            }
            else -> false
        }
    }

    override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.contextual_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    * contextual menu ends
    * */
    interface OnFileSelectedListener {
        fun onFileSelected(fileModel: FileModel)
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
