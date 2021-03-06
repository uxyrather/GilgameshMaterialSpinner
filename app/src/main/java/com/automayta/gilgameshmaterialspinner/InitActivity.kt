package com.automayta.gilgameshmaterialspinner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.automayta.gmspinner.CoreSpinnerView
import java.util.*

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        setSupportActionBar(findViewById(R.id.toolbar))
        val noteStatusMap = HashMap<Any, Any>()
        val defaultPosition = 0
        noteStatusMap[0] = "Zero"
        noteStatusMap[1] = "One"
        noteStatusMap[2] = "Two"
        noteStatusMap[3] = "Three"
        //TODO put this in pref, use while new one is being downloaded
        var spinner: CoreSpinnerView = findViewById<CoreSpinnerView>(R.id.spinner)
        spinner.setAdapter(noteStatusMap, defaultPosition)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_init, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
