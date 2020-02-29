package com.silas.themovies.model.dto.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(@ColumnInfo(name = "id") val id: Long,
                 @ColumnInfo(name = "name") val name: String): Parcelable