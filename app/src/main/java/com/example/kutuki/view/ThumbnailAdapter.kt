package com.example.kutuki.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kutuki.R
import com.example.kutuki.databinding.ThumbnailHolderBinding
import com.example.kutuki.model.VideosModel
import timber.log.Timber
import kotlin.properties.Delegates

class ThumbnailAdapter(val onVideoClicked: (category: String) -> Unit) : RecyclerView.Adapter<ThumbnailAdapter.ThumbnailHolder>(){

    lateinit var binding: ThumbnailHolderBinding

    private var videos: List<VideosModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ThumbnailHolder(val binding: ThumbnailHolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThumbnailAdapter.ThumbnailHolder {
        binding = ThumbnailHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThumbnailHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbnailAdapter.ThumbnailHolder, position: Int) {
        with(holder) {
            with(videos[position]) {
                with(binding) {
                    ivThumbnail.load(videos[position].thumbnailURL) {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                    }
                    holder.itemView.setOnClickListener{
                        onVideoClicked.invoke(videos[position].videoURL)
                    }
                }
            }
        }
    }

    private fun onViewClicked(position: Int) {
        Timber.d("CategoryClicked ${videos[position].title}")
        onVideoClicked.invoke(videos[position].title)
    }

    fun updateData(mVideos: List<VideosModel>) {
        videos = mVideos
        Timber.d("HITHEREFROMUPDATEDATA","")
    }

    override fun getItemCount(): Int {
        return videos.count()
    }

}