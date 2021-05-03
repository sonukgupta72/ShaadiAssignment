package com.sonukgupta72.shaadiassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sonukgupta72.shaadiassignment.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        // Adding Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
