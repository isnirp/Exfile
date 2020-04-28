package com.flimbis.exfile.view

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.adapter.HomeAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var listFiles: RecyclerView
    private lateinit var adapter: HomeAdapter
    private var listener: OnHomeItemsClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        var items = listOf<FileModel>(
                FileModel(path = Environment.getExternalStorageDirectory().absolutePath, isDirectory = true, name = "Main Storage", size = 0, ext = null, lastModified = null),
                FileModel(path = Environment.getExternalStorageDirectory().absolutePath, isDirectory = true, name = "Downloads", size = 0, ext = null, lastModified = null)
        )

        listFiles = view.findViewById(R.id.lst_home)
        listFiles.layoutManager = LinearLayoutManager(context)

        adapter = HomeAdapter { listener!!.onHomeItemClicked(it) }
        adapter.updateHomeItems(items)

        listFiles.adapter = adapter
        // Inflate the layout for this fragment
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as? OnHomeItemsClickListener
        if (listener == null)
            throw ClassCastException("$context must implement OnHomeItemsClickListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnHomeItemsClickListener {
        fun onHomeItemClicked(path: String)
    }
}
