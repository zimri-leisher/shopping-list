package com.leisherz.shoppinglist.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leisherz.shoppinglist.ScrollingActivity
import com.leisherz.shoppinglist.databinding.ListItemBinding
import com.leisherz.todorecyclerview.touch.RecyclerViewTouchHandler
import kotlin.concurrent.thread

class ShoppingListAdapter(val context: Context) :
    ListAdapter<ShoppingListItem, ShoppingListAdapter.ViewHolder>(ShoppingListItemDifferenceCallback()),
    RecyclerViewTouchHandler {

    inner class ViewHolder(val itemBinding: ListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: ShoppingListItem) {
            itemBinding.itemName.text = item.name
            itemBinding.purchased.isChecked = item.purchased
            itemBinding.itemDescription.text = item.desc
            itemBinding.itemPrice.text = item.estimatedPrice.toString()
            itemBinding.itemCategoryIcon.setImageResource(item.category.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val todoRowBinding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(todoRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.itemBinding.btnDelete.setOnClickListener {
            removeListItem(holder.adapterPosition)
        }
        holder.itemBinding.purchased.setOnClickListener {
            item.purchased = holder.itemBinding.purchased.isChecked
            thread {
                AppDatabase.getInstance(context).getDao().update(item)
            }
        }
        holder.itemBinding.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showDialogEditItem(item)
        }
        holder.bind(item)
    }

    fun removeListItem(position: Int) {
        thread {
            AppDatabase.getInstance(context).getDao().delete(getItem(position))
        }
    }

    override fun onDismissed(position: Int) {
        removeListItem(position)
    }

    override fun onMove(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }
}

class ShoppingListItemDifferenceCallback : DiffUtil.ItemCallback<ShoppingListItem>() {
    override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem == newItem
    }
}