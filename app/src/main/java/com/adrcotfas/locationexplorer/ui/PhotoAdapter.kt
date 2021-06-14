package com.adrcotfas.locationexplorer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.adrcotfas.locationexplorer.R
import com.adrcotfas.locationexplorer.room.PhotoUrlEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    var data: List<PhotoUrlEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(parent)

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    class PhotoViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val root: LinearLayout = itemView.findViewById(R.id.root)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(photoUrl: PhotoUrlEntity) {
            Glide.with(root.context)
                .load(photoUrl.url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(image)
        }

        companion object {
            operator fun invoke(parent: ViewGroup): PhotoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.image_item, parent, false)
                return PhotoViewHolder(view)
            }
        }

    }
}