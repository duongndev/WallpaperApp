package com.duongnd.wallpaper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "images")
@Parcelize
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val imgRes: Int,
    var isFavorite: Boolean = false
) : Parcelable {
    @Ignore
    var isSelected: Boolean = false
}
