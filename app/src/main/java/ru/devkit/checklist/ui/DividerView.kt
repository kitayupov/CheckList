package ru.devkit.checklist.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.UiContext
import ru.devkit.checklist.R

class DividerView @JvmOverloads constructor(
    @UiContext context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val checkBox: CheckBox by lazy { findViewById(R.id.check_box) }
    val counter: TextView by lazy { findViewById(R.id.counter) }
    val clickable: View by lazy { findViewById(R.id.clickable) }

    init {
        LayoutInflater.from(context).inflate(R.layout.list_item_divider, this)
        clickable.setOnClickListener { checkBox.performClick() }
    }
}