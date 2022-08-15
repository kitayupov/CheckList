package ru.devkit.checklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.devkit.checklist.ui.checklist.CheckListFragment

class MainActivity : AppCompatActivity() {

    private val router by lazy { (application as App).router }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        router.attach(supportFragmentManager)
        setupCheckList()
    }

    override fun onPause() {
        super.onPause()
        router.detach()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupCheckList() {
        val fragment = CheckListFragment().apply {
            checkListPresenter = (application as App).checkListPresenter
            actionModePresenter = (application as App).actionModePresenter
            createItemActionPresenter = (application as App).createItemActionPresenter
            router = this@MainActivity.router
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CheckListFragment.TAG)
            .commit()
    }
}