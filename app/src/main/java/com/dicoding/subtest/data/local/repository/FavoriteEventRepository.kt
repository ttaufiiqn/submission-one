package com.dicoding.subtest.data.local.repository

import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.room.FavoriteEventDao

class FavoriteEventRepository(private val favoriteEventDao: FavoriteEventDao) {
    fun getAllFavoriteEvents() = favoriteEventDao.getAllFavoriteEvents()
    fun getFavoriteEventById(id: String) = favoriteEventDao.getFavoriteEventById(id)
    suspend fun insert(favoriteEvent: FavoriteEvent) = favoriteEventDao.insert(favoriteEvent)
    suspend fun delete(favoriteEvent: FavoriteEvent) = favoriteEventDao.delete(favoriteEvent)
}
