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
import java.lang.RuntimeException
import java.util.*

class DialogEditItem(val itemBeingEdited: ShoppingListItem) : DialogFragment() {

    lateinit var handler: EditShoppingListItemHandler
    lateinit var selectedCategory: ShoppingListItemCategory

    interface EditShoppingListItemHandler {
        fun handleEditShoppingListItem(item: ShoppingListItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is EditShoppingListItemHandler) {
            handler = context
        } else {
            throw RuntimeException("The activity does not implement EditShoppingListItemHandler")
        }
    }

    lateinit var binding: DialogItemDetailsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        binding = DialogItemDetailsBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)
        binding.itemName.setText(itemBeingEdited.name)
        binding.itemDescription.setText(itemBeingEdited.desc)
        binding.itemPrice.setText(itemBeingEdited.estimatedPrice.toString())
        binding.isPurchased.isChecked = itemBeingEdited.purchased
        selectedCategory = itemBeingEdited.category
        // TODO other fields
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
                selectedCategory = itemBeingEdited.category
            }
        }
        dialogBuilder.setTitle("Edit Shopping List Item")
        dialogBuilder.setPositiveButton("Save") { dialog, which ->
            val item = itemBeingEdited.copy(
                name = binding.itemName.text.toString(),
                desc = binding.itemDescription.text.toString(),
                estimatedPrice = binding.itemPrice.text.toString().toFloat(),
                purchased = binding.isPurchased.isChecked,
                category = selectedCategory
            )
            handler.handleEditShoppingListItem(item)
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
        }
        return dialogBuilder.create()
    }
}