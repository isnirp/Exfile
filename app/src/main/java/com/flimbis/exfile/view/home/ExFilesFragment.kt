package com.flimbis.exfile.view.home


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.Toast

import com.flimbis.exfile.R
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.adapter.ExFileAdapter
import java.io.File
import androidx.appcompat.widget.PopupMenu
import android.content.IntentFilter
import com.flimbis.exfile.common.ExFileBroadcastReceiver
import com.flimbis.exfile.util.getFilesFromPath
import android.content.Intent
import android.content.BroadcastReceiver
import android.net.Uri
import android.os.Environment
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 *major lifecycle methods of a fragment to implement; onCreate, onCreateView, onPause
 */
class ExFilesFragment : androidx.fragment.app.Fragment(), ExFileAdapter.OnFileClickedListener {

    private lateinit var listFiles: ListView
    private lateinit var adapter: ExFileAdapter

    private var listener: OnFileSelectedListener? = null
    private lateinit var bReceiver: BroadcastReceiver

    companion object {
        private const val PATH = "com.flimbis.exfile.PATH_FINDER"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
        var currentDirectory: String? = null //tracks file path (directory)
    }

    /*
    * initialize essential components of the fragment that you want to retain
    * when the fragment is paused or stopped, then resumed.
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //update current path
        currentDirectory = arguments!!.getString(PATH)

        bReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, i: Intent) {
                when {
                    i.getStringExtra(ExFileBroadcastReceiver.BROADCAST_TYPE) == "EX_COPY" -> {
                        Toast.makeText(context, "copied to clipboard", Toast.LENGTH_SHORT).show()
                        listener!!.onClipboardActivated()
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
        val view = inflater.inflate(R.layout.fragment_files_ex, container, false)

        setHasOptionsMenu(true)

        listFiles = view.findViewById(R.id.lst_ex_files)

        adapter = ExFileAdapter(this, context)
        adapter.updateDirectory(getFileModelList(arguments!!.getString(PATH)))

        adapter.setFileClickedListener(this)

        listFiles.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(bReceiver, IntentFilter(ExFileBroadcastReceiver.DIR_UPDATE))
    }

    /*
    * commit any changes that should be persisted beyond the current user session (because the user might not come back)
    * */
    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(bReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                listener!!.onItemSearchSelected()
                true
            }
            R.id.action_new_folder -> {
                listener!!.onItemCreateFolderSelected()
                true
            }
            R.id.action_new_file -> {
                listener!!.onItemCreateFileSelected()
                true
            }
            R.id.action_view_list -> {
                listener!!.onItemViewSelected()
                true
            }
            R.id.action_view_grid -> {
                listener!!.onItemViewGridSelected()
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
        listener!!.onItemFileSelected(fileModel)
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
        if (fileModel.isDirectory)
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

    fun initMultiChoiceMode(listener: AbsListView.MultiChoiceModeListener) {
        listFiles.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listFiles.setMultiChoiceModeListener(listener)
    }

    fun actionModeActivated(fileModel: FileModel) {
        listener!!.onActionModeActivated(fileModel)
    }

    private fun getFileModelList(path: String): List<FileModel> {
        var files: List<File> = getFilesFromPath(path)
        return files.map { FileModel(path = it.path, isDirectory = it.isDirectory, name = it.name, size = it.length(), ext = it.extension, lastModified = it.lastModified()) }
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
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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

        fun onItemViewSelected()

        fun onItemViewGridSelected()

        fun onItemCreateFolderSelected()

        fun onItemCreateFileSelected()

        fun onItemSearchSelected()

        fun onClipboardActivated()

        fun onActionModeActivated(fileModel: FileModel)
    }


    class Builder {
        var path: String = Environment.getExternalStorageDirectory().absolutePath
        //var path: String = " "

        fun build(): ExFilesFragment {
            val fragment = ExFilesFragment()
            val args = Bundle()
            args.putString(PATH, path)
            fragment.arguments = args
            return fragment
        }
    }

}
