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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var items = mutableListOf<FileModel>()
    private lateinit var listFiles: RecyclerView
    private lateinit var adapter: HomeAdapter
    private var listener: OnHomeItemsClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        items.add(FileModel(path = Environment.getExternalStorageDirectory().absolutePath, isDirectory = true, name = "Main Storage", size = 0, ext = null, lastModified = null))
        items.add(FileModel(path = Environment.getExternalStorageDirectory().absolutePath, isDirectory = true, name = "Downloads", size = 0, ext = null, lastModified = null))

        listFiles = view.findViewById(R.id.lst_home)
        listFiles.layoutManager = LinearLayoutManager(context)

        adapter = HomeAdapter { listener!!.onHomeItemClicked(it) }
        adapter.updateHomeItems(items)

        listFiles.adapter = adapter
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(path: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, path)
                    }
                }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnHomeItemsClickListener) listener = context
        else throw RuntimeException(context.toString() + " must implement OnHomeItemsClickListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnHomeItemsClickListener {
        fun onHomeItemClicked(path: String)
    }
}
