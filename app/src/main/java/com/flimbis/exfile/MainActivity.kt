package com.flimbis.exfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.flimbis.exfile.model.FileModel
import com.flimbis.exfile.view.home.ExFilesFragment

class MainActivity : AppCompatActivity(), ExFilesFragment.OnFileSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val exFilesFragment = ExFilesFragment.build { path = Environment.getExternalStorageDirectory().absolutePath }
        //transactions; add, remove, replace
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, exFilesFragment)
        fragmentTransaction.commit()//apply fragment
    }

    override fun onFileSelected(fileModel: FileModel) {
        Toast.makeText(this,fileModel.name, Toast.LENGTH_SHORT).show()
        toFolder(fileModel.path)
    }

    private fun toFolder(folder: String) {
        val exFilesFragment = ExFilesFragment.build { path = folder }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, exFilesFragment)
        fragmentTransaction.commit()

    }
}
