package com.duongnd.wallpaper.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.duongnd.wallpaper.domain.model.ImageEntity

class ImageDiffCallback : DiffUtil.ItemCallback<ImageEntity>(){
    override fun areItemsTheSame(
        oldItem: ImageEntity,
        newItem: ImageEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ImageEntity,
        newItem: ImageEntity
    ): Boolean {
        return oldItem == newItem
    }
}