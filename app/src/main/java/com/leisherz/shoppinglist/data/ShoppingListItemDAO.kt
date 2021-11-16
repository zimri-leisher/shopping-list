package com.leisherz.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListItemDAO {
    @Query("SELECT * FROM shoppingListItems")
    fun getAll(): LiveData<List<ShoppingListItem>>

    @Query("DELETE FROM shoppingListItems")
    fun deleteAll()

    @Insert
    fun add(item: ShoppingListItem): Long

    @Delete
    fun delete(item: ShoppingListItem)

    @Update
    fun update(item: ShoppingListItem)
}