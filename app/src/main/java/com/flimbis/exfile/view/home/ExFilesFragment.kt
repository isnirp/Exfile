package com.flimbis.exfile.view.home


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.flimbis.exfile.R
import com.flimbis.exfile.ctx
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.adapter.FileAdapter
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *major lifecycle methods of a fragment to implement; onCreate, onCreateView, onPause
 */
class ExFilesFragment : androidx.fragment.app.Fragment(), FileAdapter.OnItemClickListener {

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

    private fun showMsg(msg: String){
        //longToast(msg)
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

    private fun getFilesFromPath(path: String): List<File> {
        val file = File(path)
        return file.listFiles().toList()
    }

    private fun getFileModelList(path: String): List<FileModel> {
        var files: List<File> = getFilesFromPath(path)
        return files.map { FileModel(path = it.path, isDirectory = it.isDirectory, name = it.name, size = it.totalSpace, ext = it.extension, lastModified = it.lastModified()) }
    }

    interface OnFileSelectedListener {
        fun onFileSelected(fileModel: FileModel)
    }


    class Builder{
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
