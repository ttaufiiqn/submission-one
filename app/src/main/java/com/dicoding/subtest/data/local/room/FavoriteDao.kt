package com.dicoding.subtest.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.subtest.data.local.entity.FavoriteEvent


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteEvent)
    @Update
    fun update(favorite: FavoriteEvent)
    @Delete
    fun delete(favorite: FavoriteEvent)
    @Query("SELECT * from FavoriteEvent ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<FavoriteEvent>>
}