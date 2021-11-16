package com.leisherz.shoppinglist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.leisherz.shoppinglist.R
import com.leisherz.shoppinglist.data.ShoppingListItem
import com.leisherz.shoppinglist.data.ShoppingListItemCategory
import com.leisherz.shoppinglist.databinding.DialogItemDetailsBinding
import java.lang.NumberFormatException
import java.lang.RuntimeException

class DialogNewItem : DialogFragment() {

    lateinit var handler: NewShoppingListItemHandler
    lateinit var selectedCategory: ShoppingListItemCategory

    interface NewShoppingListItemHandler {
        fun handleNewShoppingListItem(item: ShoppingListItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is NewShoppingListItemHandler) {
            handler = context
        } else {
            throw RuntimeException("The activity does not implement NewShoppingListItemHandler")
        }
    }

    lateinit var binding: DialogItemDetailsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        binding = DialogItemDetailsBinding.inflate(layoutInflater)
        val spinner = binding.categorySelection
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = ShoppingListItemCategory.values().first {
                    it.name.lowercase() == parent!!.getItemAtPosition(position).toString()
                        .lowercase()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = ShoppingListItemCategory.BOOK
            }
        }
        dialogBuilder.setView(binding.root)
        dialogBuilder.setTitle("New Shopping List Item")
        dialogBuilder.setPositiveButton("Add") { dialog, which ->
            try {
                val item = ShoppingListItem(
                    null,
                    selectedCategory,
                    binding.itemName.text.toString(),
                    binding.itemDescription.text.toString(),
                    binding.itemPrice.text.toString().toFloat(),
                    binding.isPurchased.isChecked
                )
                handler.handleNewShoppingListItem(item)
            } catch (e: NumberFormatException) {
                val item = ShoppingListItem(
                    null,
                    selectedCategory,
                    binding.itemName.text.toString(),
                    binding.itemDescription.text.toString(),
                    0f,
                    binding.isPurchased.isChecked
                )
                handler.handleNewShoppingListItem(item)
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
        }
        return dialogBuilder.create()
    }
}