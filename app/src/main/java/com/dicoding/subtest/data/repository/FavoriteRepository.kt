package com.dicoding.subtest.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.room.FavoriteDao
import com.dicoding.subtest.data.local.room.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.FavoriteDao()
    }
    fun getAllFavorite(): LiveData<List<FavoriteEvent>> = mFavoriteDao.getAllFavorite()
    fun insert(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.insert(favoriteEvent) }
    }
    fun delete(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.delete(favoriteEvent) }
    }
    fun update(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteDao.update(favoriteEvent) }
    }
}
