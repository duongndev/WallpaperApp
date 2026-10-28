package com.duongnd.wallpaper.component.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.adapter.BaseAdapter
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.databinding.ItemImageBinding

class ImageAdapter : BaseAdapter<ImageEntity, ItemImageBinding>() {

    var onFavoriteClick: ((ImageEntity) -> Unit)? = null

    override fun setData(listItem: ArrayList<ImageEntity>) {
        super.setData(listItem)
    }

    override fun provideViewBinding(parent: ViewGroup): ItemImageBinding {
        return ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemImageBinding, item: ImageEntity, position: Int) {

        viewBinding.apply {
//            ivWallpaper.setImageResource(item.imgRes)

            Glide.with(root.context).load(item.imgRes).into(ivWallpaper)


            if (item.isFavorite) {
                imFav.setImageResource(R.drawable.ic_favorite_selected)
            } else {
                imFav.setImageResource(R.drawable.ic_favorite)
            }
            imFav.setOnClickListener {
                onFavoriteClick?.invoke(item)
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
