package com.flimbis.exfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.home.ExFilesFragment

import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import com.flimbis.exfile.databinding.ItemsFileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File
import kotlinx.android.synthetic.main.bottom_sheet.*

//class MainActivity : AppCompatActivity(), ExFilesFragment.OnFileSelectedListener, AbsListView.MultiChoiceModeListener {
class MainActivity : AppCompatActivity(), ExFilesFragment.OnFileSelectedListener, ActionMode.Callback {
    lateinit var sheetBehaviour: BottomSheetBehavior<LinearLayout>
    var currentIndex: Int? = null
    var actionMode: ActionMode? = null
    //lateinit var exFilesFragment: ExFilesFragment
    lateinit var mFileModel: FileModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_White)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar_main) as Toolbar
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
           val exFilesFragment = ExFilesFragment.build { path = Environment.getExternalStorageDirectory().absolutePath }

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
            fragmentTransaction.commit()//apply fragment

        }

        //exFilesFragment.initMultiChoiceMode(this)

        val bottom_sheet = findViewById<LinearLayout>(R.id.lnr_bottom_sheet)
        sheetBehaviour = BottomSheetBehavior.from(bottom_sheet)


    }

    override fun onBackPressed() {
        super.onBackPressed()
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
            R.id.menu_cut -> {
                //shareCurrentItem()
                Toast.makeText(this, mFileModel.name, Toast.LENGTH_SHORT).show()
                mode?.finish() // Action picked, so close the CAB
                true
            }
            R.id.menu_copy -> {
                Toast.makeText(this, mFileModel.path, Toast.LENGTH_SHORT).show()
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

    override fun onFileSelected(fileModel: FileModel) {
        if (fileModel.isDirectory)
            toFolder(fileModel.path)
        else toFileIntent(fileModel.path)
        //Toast.makeText(this, fileModel.name, Toast.LENGTH_SHORT).show()
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

    override fun onActionModeActivated(fileModel: FileModel) {
        actionMode = startActionMode(this)
        this.mFileModel = fileModel
    }

    private fun toFolder(folder: String) {
        val exFilesFragment = ExFilesFragment.build { path = folder }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, exFilesFragment)
        fragmentTransaction.addToBackStack(folder)
        fragmentTransaction.commit()

    }

    private fun toFileIntent(path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri: Uri = getUriForFile(this, packageName, File(path))
        intent.data = contentUri
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(intent, "Select Application"))
    }
}
