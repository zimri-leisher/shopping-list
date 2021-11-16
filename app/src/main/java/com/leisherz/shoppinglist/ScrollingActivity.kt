package com.leisherz.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leisherz.shoppinglist.data.AppDatabase
import com.leisherz.shoppinglist.data.ShoppingListAdapter
import com.leisherz.shoppinglist.data.ShoppingListItem
import com.leisherz.shoppinglist.databinding.ActivityScrollingBinding
import com.leisherz.shoppinglist.dialog.DialogEditItem
import com.leisherz.shoppinglist.dialog.DialogNewItem
import com.leisherz.todorecyclerview.touch.RecyclerViewTouchCallback
import kotlin.concurrent.thread

class ScrollingActivity : AppCompatActivity(), DialogNewItem.NewShoppingListItemHandler,
    DialogEditItem.EditShoppingListItemHandler {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var recyclerViewAdapter: ShoppingListAdapter

    var currentTotal = 0f
        set(value) {
            field = value
            binding.toolbarLayout.title = "${getString(R.string.total)}: $$value"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.fabNew.setOnClickListener { view ->
            showDialogNewItem()
        }
        binding.fabClear.setOnClickListener { view ->
            thread {
                AppDatabase.getInstance(this).getDao().deleteAll()
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val listItems = AppDatabase.getInstance(this).getDao().getAll()
        recyclerViewAdapter = ShoppingListAdapter(this)
        binding.shoppingListRecycler.adapter = recyclerViewAdapter
        listItems.observe(this) {
            recyclerViewAdapter.submitList(it)
            currentTotal = it.fold(0f, { acc, item -> acc + item.estimatedPrice })
        }

        val touchCallbackList = RecyclerViewTouchCallback(recyclerViewAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.shoppingListRecycler)

        binding.shoppingListRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    fun showDialogNewItem() {
        val dialog = DialogNewItem()
        dialog.show(supportFragmentManager, "DIALOG_NEW_ITEM")
    }

    fun showDialogEditItem(item: ShoppingListItem) {
        val dialog = DialogEditItem(item)
        dialog.show(supportFragmentManager, "DIALOG_EDIT_ITEM")
    }

    override fun handleNewShoppingListItem(item: ShoppingListItem) {
        thread {
            AppDatabase.getInstance(this).getDao().add(item).apply { item.id = this }
        }
//        currentTotal += item.estimatedPrice
        Snackbar.make(binding.root, "Item created", Snackbar.LENGTH_LONG).setAction("Undo") {
            recyclerViewAdapter.removeListItem(recyclerViewAdapter.itemCount - 1)
        }.show()
    }

    override fun handleEditShoppingListItem(item: ShoppingListItem) {
        thread {
            AppDatabase.getInstance(this).getDao().update(item)
        }
    }

}