package com.example.coinhakodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coinhakodemo.base.BaseActivity
import com.example.coinhakodemo.module.home.HomeFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.am_content, HomeFragment.newInstance(), HomeFragment::class.java.simpleName).commit()

    }
}