package com.leisherz.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leisherz.shoppinglist.R

enum class ShoppingListItemCategory(val icon: Int) {
    FOOD(R.drawable.ic_baseline_fastfood_24),
    DRINK(R.drawable.ic_baseline_local_drink_24),
    BOOK(R.drawable.ic_baseline_menu_book_24)
}

@Entity(tableName = "shoppingListItems")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "category")
    var category: ShoppingListItemCategory,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "desc")
    var desc: String,
    @ColumnInfo(name = "estimatedPrice")
    var estimatedPrice: Float,
    @ColumnInfo(name = "purchased")
    var purchased: Boolean)