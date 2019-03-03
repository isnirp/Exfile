package com.flimbis.exfile.view.home


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
 *
 */
class HomeFragment : Fragment() {
    lateinit var listHomeFiles: RecyclerView
    lateinit var adapter: FileAdapter
    var filesList = listOf<FileModel>()
    val PATH = Environment.getExternalStorageDirectory().absolutePath

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)

        adapter.fetchFiles(getFileModelList())
        return view
    }

    private fun initView(view: View) {
        listHomeFiles = view.findViewById(R.id.lst_home_files)
        listHomeFiles.layoutManager = LinearLayoutManager(view.ctx)
        adapter = FileAdapter()
        listHomeFiles.adapter = adapter
    }

    private fun getFilesFromPath(path: String): List<File> {
        val file = File(path)
        return file.listFiles().toList()
    }

    private fun getFileModelList(): List<FileModel> {
        var files: List<File> = getFilesFromPath(PATH)
        return files.map { FileModel(path = it.path, name = it.name, ext = it.extension) }
    }
}
