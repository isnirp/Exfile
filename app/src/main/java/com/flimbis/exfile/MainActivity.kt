package com.flimbis.exfile

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.home.ExFilesFragment

import android.net.Uri
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flimbis.exfile.common.BackStackManager
import com.flimbis.exfile.common.ExFileBroadcastReceiver
import com.flimbis.exfile.model.FileType
import com.flimbis.exfile.service.ExFileJobService
import com.flimbis.exfile.util.*
import com.flimbis.exfile.view.adapter.BreadcrumbAdapter
import com.flimbis.exfile.view.dialog.CreateFolderDialog
import com.flimbis.exfile.view.dialog.DeleteDialog
import com.flimbis.exfile.view.home.ExFilesFragment.Companion.currentDirectory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet_views.*

//class MainActivity : AppCompatActivity(), ExFilesFragment.OnFileSelectedListener, AbsListView.MultiChoiceModeListener {
class MainActivity : AppCompatActivity(), ExFilesFragment.OnFileSelectedListener, ActionMode.Callback, CreateFolderDialog.OnCreateDialogClickListener, DeleteDialog.OnDeleteDialogClickListener {
    lateinit var sheetBehaviour: BottomSheetBehavior<LinearLayout>

    lateinit var mFileModel: FileModel
    lateinit var clipboard: ClipboardManager
    var actionMode: ActionMode? = null
    private lateinit var breadcrumbAdapter: BreadcrumbAdapter
    private val backStackManager = BackStackManager()
    private lateinit var breadcrumbRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_White)
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val exFilesFragment = ExFilesFragment.build { path = Environment.getExternalStorageDirectory().absolutePath }
            //val exFilesFragment = ExFilesFragment.build { path = "/" }

            //transactions; add, remove, replace
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.container, exFilesFragment)
            /*
            *The tag string in addToBackStack(String name) gives a way to locate the back stack for later pop directly to that location.
            *It meant to be used in the method popToBackStack(String name, int flags)
            *
            * In other words, it will pop your back stack until it finds the fragment that was added by the name in addToBackStack(String name)
            * */
            fragmentTransaction.addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
            //fragmentTransaction.addToBackStack("/")
            fragmentTransaction.commit()//apply fragment

        }
        initViews()
        initBackStack()

        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //exFilesFragment.initMultiChoiceMode(this)

        val bottomSheet = findViewById<LinearLayout>(R.id.lnr_bottom_sheet)
        sheetBehaviour = BottomSheetBehavior.from(bottomSheet)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        backStackManager.popFromStack()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    /*
    * contextual action bar
    * */
    /*override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_cut -> {
                //shareCurrentItem()
                mode?.finish() // Action picked, so close the CAB
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
        mode?.setTitle("Actions")
        val inflater: MenuInflater? = mode?.menuInflater
        inflater?.inflate(R.menu.contextual_menu, menu)

        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/
    //Action mode
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            /* R.id.menu_cut -> {
                 //shareCurrentItem()
                 Toast.makeText(this, currentDirectory!!, Toast.LENGTH_SHORT).show()
                 mode?.finish() // Action picked, so close the CAB
                 true
             }*/
            R.id.menu_copy -> {
                //Toast.makeText(this, mFileModel.path, Toast.LENGTH_SHORT).show()
                val contentUri: Uri = FileProvider.getUriForFile(this, "com.flimbis.exfile.MyFileProvider", File(mFileModel.path))
                copyFileToDirectory(contentUri, this)

                if (clipboard.hasPrimaryClip()) {
                    sendBroadcastIntent(currentDirectory!!, "EX_COPY")
                }

                mode?.finish()
                true
            }
            R.id.menu_delete -> {
                /*if (mFileModel.isDirectory) {
                    if (deleteDirectory(mFileModel.path)) sendBroadcastIntent(currentDirectory!!)
                    else
                        Toast.makeText(this, "Failed to delete directory", Toast.LENGTH_SHORT).show()
                } else {
                    if (deleteFileAtPath(mFileModel.path)) sendBroadcastIntent(currentDirectory!!)
                    else
                        Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show()
                }*/
                val filesToDelete = mutableListOf<FileModel>()
                filesToDelete.add(mFileModel)

                val openDeleteDialog = DeleteDialog(filesToDelete)
                openDeleteDialog.show(supportFragmentManager, "delete files")
                mode?.finish() // Action picked, so close the CAB
                true
            }
            else -> false
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.setTitle("Actions")
        val inflater: MenuInflater? = mode?.menuInflater
        inflater?.inflate(R.menu.contextual_menu, menu)

        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false // Return false if nothing is done
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
    }
    /*
    * contextual action bar ends
    * */

    override fun onItemFileSelected(fileModel: FileModel) {
        if (fileModel.isDirectory) toFolder(fileModel.path)
        else toFileIntent(fileModel.path)
    }

    override fun onItemPropertySelected(fileModel: FileModel) {

        prop_name.text = fileModel.name
        prop_type.text = "file"
        prop_path.text = fileModel.path
        prop_size.text = fileModel.size.toString()
        prop_folder_quantity.text = "Empty"
        prop_created.text = "23/12/2019"
        prop_last_modified.text = "23/12/2019"

        sheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onItemRenameSelected(fileModel: FileModel) {
        val openCreateFolderDialog = CreateFolderDialog(2)
        openCreateFolderDialog.setPreviousName(fileModel.name)
        openCreateFolderDialog.show(supportFragmentManager, "rename to")
    }

    override fun onItemViewSelected(filePath: String, viewType: Int) {
        //persist viewType
        upDateViewTypePref(viewType)

        //recreate view
        val exFilesFragment = ExFilesFragment.build { path = filePath }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, exFilesFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        /*val sheetDialog = BottomSheetDialog(this)
        val sheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_views, null)
        sheetDialog.setContentView(sheetView)

        val selectListView = sheetView.findViewById<LinearLayout>(R.id.select_list_view)
        val selectDetailView = sheetView.findViewById<LinearLayout>(R.id.select_detail_view)
        val selectGridView = sheetView.findViewById<LinearLayout>(R.id.select_grid_view)

        selectListView.setOnClickListener(View.OnClickListener { Toast.makeText(this, "list selected", Toast.LENGTH_SHORT).show() })

        sheetDialog.show()*/

    }

    override fun onItemCreateFolderSelected() {
        val openCreateFolderDialog = CreateFolderDialog(0)
        openCreateFolderDialog.show(supportFragmentManager, "create folder")
    }

    override fun onItemCreateFileSelected() {
        val openCreateFileDialog = CreateFolderDialog(1)
        openCreateFileDialog.show(supportFragmentManager, "create file")
    }

    override fun onItemSearchSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClipboardActivated() {
    }

    override fun onActionModeActivated(fileModel: FileModel) {
        actionMode = startActionMode(this)
        this.mFileModel = fileModel
    }

    override fun onCreateFolder(name: String) {
        //scheduleJob(currentPath!!, name, "FOLDER")
        if (createFolderAtDirectory(currentDirectory!!, name)) sendBroadcastIntent(currentDirectory!!, "EX_CREATE")
        else
            Toast.makeText(this, "Failed to create directory ${name}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateFile(name: String) {
        //scheduleJob(currentPath!!, name, "FILE")
        if (createFileAtDirectory(currentDirectory!!, name)) sendBroadcastIntent(currentDirectory!!, "EX_CREATE")
        else
            Toast.makeText(this, "Failed to create file ${name}", Toast.LENGTH_SHORT).show()
    }

    override fun onRenameFile(prevName: String, name: String) {
        if (renameFileAtDirectory(currentDirectory!!, prevName, name))
            sendBroadcastIntent(currentDirectory!!, "EX_RENAME")
    }

    override fun onDeleteFile(files: List<FileModel>) {
        for (fileModel in files) {
            if (fileModel.isDirectory) {
                deleteDirectory(fileModel.path)
            } else {
                deleteFileAtPath(fileModel.path)
            }
        }

        sendBroadcastIntent(currentDirectory!!, "EX_DELETE")
    }

    private fun initViews() {
        //setSupportActionBar(toolbar)
        breadcrumbRecyclerView = findViewById<RecyclerView>(R.id.lst_bread_crumb)
        breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        breadcrumbAdapter = BreadcrumbAdapter()
        breadcrumbRecyclerView.adapter = breadcrumbAdapter

        breadcrumbAdapter.onItemClickListener = {
            supportFragmentManager.popBackStack(it.path, 2)
            backStackManager.popFromStackTill(it)
        }
    }

    private fun updateBreadcrumbData(files: List<FileModel>) {
        breadcrumbAdapter.updateBreadcrumbList(files)
        if (files.isNotEmpty()) {
            breadcrumbRecyclerView.smoothScrollToPosition(files.size - 1)
        }
    }

    private fun initBackStack() {
        backStackManager.onStackChangeListener = {
            updateBreadcrumbData(it)
        }

        backStackManager.addToStack(fileModel = FileModel("exPath", true, "exHome", 0, "exe", 0))
    }

    private fun toFolder(filePath: String) {
        val exFilesFragment = ExFilesFragment.build { path = filePath }

        backStackManager.addToStack(getFileModel(filePath))

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, exFilesFragment)
        fragmentTransaction.addToBackStack(filePath)
        fragmentTransaction.commit()

    }

    private fun toFileIntent(path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri: Uri = getUriForFile(this, "com.flimbis.exfile.MyFileProvider", File(path))
        intent.data = contentUri
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }

    private fun getFileModel(path: String): FileModel {
        var file: File = getFileFromPath(path)
        return FileModel(path = file.path, isDirectory = file.isDirectory, name = file.name, size = file.length(), ext = file.extension, lastModified = file.lastModified())
    }

    private fun upDateViewTypePref(viewType: Int) {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.view_type_key), viewType)
            commit()
        }
    }

    private fun sendBroadcastIntent(path: String, broadcastType: String) {
        val intent = Intent(ExFileBroadcastReceiver.DIR_UPDATE)
        intent.putExtra(ExFileBroadcastReceiver.DIR_PATH_KEY, path)
        intent.putExtra(ExFileBroadcastReceiver.BROADCAST_TYPE, broadcastType)
        sendBroadcast(intent)
    }

    /*
    *broadcast is started when a file or folder is written to Uri
    * */
    //private fun scheduleBroadcastOnNewFile(path: Uri) {
    private fun scheduleBroadcastOnNewFile(path: String) {
        val contentUri: Uri = FileProvider.getUriForFile(this, "com.flimbis.exfile.MyFileProvider", File(path))
        Toast.makeText(this, "content URI " + contentUri, Toast.LENGTH_SHORT).show()
        Log.i("TAG_SCHED", "successful job-->" + contentUri)

        val bundle = PersistableBundle()
        bundle.putString("FILE_PATH", path)

//                .addTriggerContentUri(JobInfo.TriggerContentUri(contentUri, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
        val componentName = ComponentName(this, ExFileJobService::class.java)
        val jobInfo = JobInfo.Builder(111, componentName)
                .setExtras(bundle)
                .build()

        val jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i("TAG_SCHED", "successful job-->" + contentUri)
            //Toast.makeText(this, "successful job", Toast.LENGTH_SHORT).show()
        } else {
            Log.i("TAG_SCHED", "failed job")
            //Toast.makeText(this, "failed job", Toast.LENGTH_SHORT).show()
        }

    }

    private fun scheduleJob(path: String, name: String, fileType: String) {
        val bundle = PersistableBundle()
        bundle.putString("FILE_PATH", path)
        bundle.putString("FILE_NAME", name)
        bundle.putString("FILE_TYPE", fileType)

        val componentName = ComponentName(this, ExFileJobService::class.java)
        val jobInfo = JobInfo.Builder(111, componentName)
                .setExtras(bundle)
                .build()

        val jobScheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)

        if (resultCode == JobScheduler.RESULT_SUCCESS)
            Log.i("TAG_SCHED", "succesful job")
        else
            Log.i("TAG_SCHED", "failed job")

    }
}
