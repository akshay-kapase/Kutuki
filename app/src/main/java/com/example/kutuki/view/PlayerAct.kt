package com.example.kutuki.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuki.databinding.PlayerActBinding
import com.example.kutuki.utils.NetworkResult
import com.example.kutuki.viewmodel.PlayerViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlayerAct : BaseAct() {

    lateinit var binding: PlayerActBinding
    lateinit var thumbnailAdapter: ThumbnailAdapter
    var category = ""
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private val playerViewModel by viewModels<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        category = intent.getStringExtra("category")?:""
        setListView()
        fetchData()
    }

    private fun setListView() {
        val onVideoClicked: (video: String) -> Unit = { videoUrl ->
            //start video
            player?.let{
                val mediaItem = MediaItem.fromUri(videoUrl)
                it.setMediaItem(mediaItem)
            }
        }
        thumbnailAdapter = ThumbnailAdapter(onVideoClicked)
        binding.rvThumbnail.apply {
            layoutManager = LinearLayoutManager(this@PlayerAct, LinearLayoutManager.HORIZONTAL, false)
            adapter = thumbnailAdapter
        }
    }

    private fun fetchData() {
        fetchResponse()
        playerViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        thumbnailAdapter.updateData(it.response.videos.filter{
                            s -> s.categories.split(',').toTypedArray().contains(category)
                        }.apply{
                            if(this.size > 0){
                                initializePlayer(this.get(0).videoURL)
                            }
                        })
                        Timber.d("PlayerAct %s",it.response.videos.toString())
                    }
//                    binding.pbLoader.visibility = View.GONE
                }

                is NetworkResult.Error -> {
//                    binding.pbLoader.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
//                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initializePlayer(videoURL: String) {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                Timber.d("InitializePlayer $videoURL")
                binding.pvVideo.player = exoPlayer
                val mediaItem = MediaItem.fromUri(videoURL)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
            }
    }

    private fun fetchResponse() {
        playerViewModel.fetchVideos()
//        binding.pbLoader.visibility = View.VISIBLE
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

}