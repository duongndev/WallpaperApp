package com.duongnd.wallpaper.component.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.adapter.BaseAdapter
import com.duongnd.wallpaper.data.model.ImageEntity
import com.duongnd.wallpaper.databinding.ItemImageBinding

class ImageAdapter : BaseAdapter<ImageEntity, ItemImageBinding>() {

    override fun setData(listItem: ArrayList<ImageEntity>) {
        super.setData(listItem)
    }

    override fun provideViewBinding(parent: ViewGroup): ItemImageBinding {
        return ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemImageBinding, item: ImageEntity, position: Int) {

        viewBinding.apply {
            ivWallpaper.setImageResource(item.imgRes)
            if (item.isFavorite) {
                imFav.setImageResource(R.drawable.ic_favorite_selected)
            } else {
                imFav.setImageResource(R.drawable.ic_favorite)
            }
            imFav.setOnClickListener {
                onClick?.invoke(item)
            }
            root.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    fun changeFav(image: ImageEntity) {
        val index = dataSet.indexOf(image)
        if (index != -1) {
            dataSet[index].isFavorite = !dataSet[index].isFavorite
            notifyItemChanged(index)
        }
    }
}
