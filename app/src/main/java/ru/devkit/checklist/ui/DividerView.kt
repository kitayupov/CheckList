package ru.devkit.checklist.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.UiContext
import androidx.core.view.isVisible
import ru.devkit.checklist.R

class DividerView @JvmOverloads constructor(
    @UiContext context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val checkBox: CheckBox by lazy { findViewById(R.id.check_box) }
    private val counter: TextView by lazy { findViewById(R.id.counter) }

    var count: Int = 0
        set(value) {
            field = value
            counter.text = count.toString()
            isVisible = (count != 0)
        }

    var expanded: Boolean = false
        set(value) {
            field = value
            checkBox.isChecked = expanded
        }

    var expandAction: (Boolean) -> Unit = {}

    init {
        LayoutInflater.from(context).inflate(R.layout.list_item_divider, this)
        checkBox.setOnCheckedChangeListener { _, checked -> expandAction.invoke(checked) }
        val clickable = findViewById<View>(R.id.clickable)
        clickable.setOnClickListener { checkBox.performClick() }
    }
}