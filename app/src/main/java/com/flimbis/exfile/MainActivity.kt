package com.flimbis.exfile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.flimbis.exfile.ui.ListFilesFragment
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, ListFilesFragment())
        fragmentTransaction.commit()
    }
}
