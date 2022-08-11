package ru.devkit.checklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.devkit.checklist.ui.presentation.CheckListFragment

class MainActivity : AppCompatActivity() {

    private val presenter by lazy { (application as App).presenter }
    private val router by lazy { (application as App).router }

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        router.attach(supportFragmentManager)
        setupCheckList()
    }

    private fun setupCheckList() {
        val fragment = CheckListFragment().apply {
            presenter = this@MainActivity.presenter
            router = this@MainActivity.router
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CheckListFragment.TAG)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        router.detach()
    }
}