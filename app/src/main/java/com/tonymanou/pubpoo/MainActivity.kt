package com.tonymanou.pubpoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tonymanou.pubpoo.ui.list.ToiletListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ToiletListFragment.newInstance())
                    .commitNow()
        }
    }
}
