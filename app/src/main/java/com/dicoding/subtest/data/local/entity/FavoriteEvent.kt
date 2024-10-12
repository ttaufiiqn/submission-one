package com.dicoding.subtest.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "title")
    var name: String = "",
    @ColumnInfo(name = "image")
    var mediaCover: String? = null,
): Parcelable