package ru.devkit.shoppinglist.ui.additem

import androidx.fragment.app.FragmentManager

class CreateNewItemViewRouter(
    private val fragmentManager: FragmentManager
) {

    fun showCreateNewItemView(action: (String) -> Unit) {
        val dialog = CreateNewItemDialogFragment()
        dialog.action = action
        dialog.show(fragmentManager, CreateNewItemDialogFragment.TAG)
    }
}