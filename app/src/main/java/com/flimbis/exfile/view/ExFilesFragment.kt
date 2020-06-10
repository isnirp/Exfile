package com.flimbis.exfile.view


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import androidx.appcompat.widget.PopupMenu
import android.content.IntentFilter
import com.flimbis.exfile.common.ExFileBroadcastReceiver
import android.content.Intent
import android.content.BroadcastReceiver
import android.net.Uri
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.view.adapter.ExFileAdapter
import com.google.android.material.snackbar.Snackbar

import androidx.recyclerview.widget.GridLayoutManager
import com.flimbis.exfile.MainActivity
import com.flimbis.exfile.data.DataRepository
import com.flimbis.exfile.databinding.FragmentFilesExBinding
import com.flimbis.exfile.viewmodel.FileViewModel


/**
 * A simple [Fragment] subclass.
 *major lifecycle methods of a fragment to implement; onCreate, onCreateView, onPause
 */
class ExFilesFragment : androidx.fragment.app.Fragment(), ExFileAdapter.OnFileClickedListener {

    private lateinit var listFiles: RecyclerView
    private lateinit var adapter: ExFileAdapter
    private var listener: OnFileSelectedListener? = null
    private lateinit var bReceiver: BroadcastReceiver
    private lateinit var viewModel: FileViewModel
    private var items = listOf<FileModel>()
    private var selectedItems = mutableListOf<FileModel>() //for contextual bar

    companion object {
        private const val PATH = "com.flimbis.exfile.PATH_FINDER"
        //actionmode
        //var isActionMode = false
        var actionModeDismiss = 0

        /*
        * apply
        * inline fun <T> T.apply(block: T.() -> Unit): T
        * Calls the specified function block with this value as its receiver and returns this(the object) value
        * usage: initialising objects eg; Person().apply{name=Prince}
        * */
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()

        var currentDirectory: String? = null //tracks file path (directory)
    }

    /*
    * initialize essential components of the fragment that you want to retain
    * when the fragment is paused or stopped, then resumed.
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        /*activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //if (MainActivity.isInActionMode){
                    Toast.makeText(context, "Exfile", Toast.LENGTH_SHORT).show()
                    //adapter.clearSelection()
                    //listener!!.onActionModeDeActivated()
                //}
            }
        })*/

        bReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, i: Intent) {
                when {
                    i.getStringExtra(ExFileBroadcastReceiver.BROADCAST_TYPE) == "EX_COPY" -> {
                        Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show()
                        listener?.onClipboardActivated()
                    }
                    i.getStringExtra(ExFileBroadcastReceiver.BROADCAST_TYPE) == "EX_CREATE" -> {
                        adapter.updateDirectory(getFileModelList(i.getStringExtra(ExFileBroadcastReceiver.DIR_PATH_KEY)))
                        showMsg("File created success")
                    }
                    i.getStringExtra(ExFileBroadcastReceiver.BROADCAST_TYPE) == "EX_RENAME" -> {
                        adapter.updateDirectory(getFileModelList(i.getStringExtra(ExFileBroadcastReceiver.DIR_PATH_KEY)))
                        showMsg("File renamed success")
                    }
                    else -> {
                        adapter.updateDirectory(getFileModelList(i.getStringExtra(ExFileBroadcastReceiver.DIR_PATH_KEY)))
                        showMsg("File deleted success")
                    }
                }

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentFilesExBinding>(layoutInflater, R.layout.fragment_files_ex, container, false)
        val view = binding.root

        //update current path
        currentDirectory = arguments!!.getString(PATH)

        val data = DataRepository()
        viewModel = FileViewModel(data)
        binding.fileViewModel = viewModel

        listFiles = view.findViewById(R.id.lst_ex_files)

        adapter = ExFileAdapter()

        val sharedPref = context?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val viewType = sharedPref?.getInt(getString(R.string.view_type_key), 0)

        when (viewType) {
            1 -> {
                listFiles.layoutManager = GridLayoutManager(context, 3)
            }
            else -> {
                listFiles.layoutManager = LinearLayoutManager(context)
            }
        }

        /*
        * let; extension function on anytype
        * if viewType is not null execute the function
        * */
        viewType?.let { adapter.setViewType(viewType) }
        // get items at path
        items = getFileModelList(arguments!!.getString(PATH))
        adapter.updateDirectory(items)
        adapter.setFileClickedListener(this)
        Log.i("TAG_PATH", arguments!!.getString(PATH))
        Log.i("TAG_CURRENT_DIR", currentDirectory)

        listFiles.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(bReceiver, IntentFilter(ExFileBroadcastReceiver.DIR_UPDATE))
        //if (actionModeDismiss == 1)
        if (MainActivity.isInActionMode)
            adapter.clearSelection()
    }

    /*
    * commit any changes that should be persisted beyond the current user session (because the user might not come back)
    * */
    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(bReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_folder -> {
                listener?.onItemCreateFolderSelected()
                true
            }
            R.id.action_new_file -> {
                listener?.onItemCreateFileSelected()
                true
            }
            R.id.action_view_list -> {
                listener?.onItemViewSelected(arguments!!.getString(PATH))
                true
            }
            R.id.action_view_grid -> {
                listener?.onItemViewSelected(arguments!!.getString(PATH), viewType = 1)
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
            throw RuntimeException(context.toString() + " must implement OnFileSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onFileClicked(fileModel: FileModel) {
        listener?.onItemFileSelected(fileModel)
    }

    override fun onFileLongClicked(fileModel: FileModel) {
        //isActionMode = true
        listener!!.onActionModeActivated(fileModel)
    }

    override fun onPopMenuClicked(v: View, fileModel: FileModel) {
        val popup = PopupMenu(context!!, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_pop, popup.menu)

        val mimeFilter = listOf(
                "jpeg",
                "jpg",
                "png",
                "gif"
        )
        val shareMenu: MenuItem = popup.menu.findItem(R.id.menu_pop_share)
        if (fileModel.type == "folder")
            shareMenu.isVisible = false
        else {
            if (fileModel.ext !in mimeFilter) shareMenu.isVisible = false
        }

        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.menu_pop_rename -> {
                        listener?.onItemRenameSelected(fileModel)
                        popup.dismiss()
                        true
                    }
                    R.id.menu_pop_share -> {
                        shareImages(fileModel.path)
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

    override fun selectedItemsInActionMode(fileModel: FileModel, size: Int) {
        if (0 == size ) {
            //isActionMode = false
            adapter.clearSelection()
            listener!!.onActionModeDeActivated()
        } else
            listener!!.updateActionModeSelection(fileModel, "$size")

    }

    private fun getFileModelList(path: String): List<FileModel> {
        return viewModel.getFiles(path)
    }

    private fun shareImages(path: String) {

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
            type = "image/*"
        }
        context?.startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
    }

    private fun showMsg(msg: String) {
        Snackbar.make(listFiles, msg, Snackbar.LENGTH_SHORT)
                .show()
    }


    /*private fun scheduleBroadcastOnNewFile(path: String) {
        val contentUri: Uri = Uri.parse("content://com.flimbis.exfile.MyFileProvider/external_files/Pictures/")
        //val contentUri: Uri = FileProvider.getUriForFile(context!!, "com.flimbis.exfile.MyFileProvider", File(path))
        //Toast.makeText(context, "content URI " + contentUri, Toast.LENGTH_SHORT).show()

        val bundle = PersistableBundle()
        bundle.putString("FILE_PATH", path)

        val componentName = ComponentName(context, ExFileJobService::class.java)

        val jobInfo = JobInfo.Builder(1111, componentName)
                .addTriggerContentUri(JobInfo.TriggerContentUri(contentUri, 0))
                .addTriggerContentUri(JobInfo.TriggerContentUri(contentUri, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                .build()

        val jobScheduler: JobScheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i("TAG_SCHED", "successful job-->" + contentUri)
            Toast.makeText(context, "successful job", Toast.LENGTH_SHORT).show()
        } else {
            Log.i("TAG_SCHED", "failed job")
            //Toast.makeText(this, "failed job", Toast.LENGTH_SHORT).show()
        }

    }*/

    interface OnFileSelectedListener {
        fun onItemFileSelected(fileModel: FileModel)

        fun onItemPropertySelected(fileModel: FileModel)

        fun onItemRenameSelected(fileModel: FileModel)

        fun onItemViewSelected(filePath: String, viewType: Int = 0)

        fun onItemCreateFolderSelected()

        fun onItemCreateFileSelected()

        fun onItemSearchSelected()

        fun onClipboardActivated()

        fun onActionModeActivated(fileModel: FileModel)

        fun onActionModeDeActivated()

        fun updateActionModeSelection(fileModel: FileModel, title: String)
    }

    class Builder {
        var path: String = " "

        fun build(): ExFilesFragment = ExFilesFragment().apply {
            arguments = Bundle().apply { putString(PATH, path) }
        }

    }

}
