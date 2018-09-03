package com.ioraptor.exoplayerpoc.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class PlayerViewModel(application: Application): AndroidViewModel(application) {

    private val bandwidthMeter = DefaultBandwidthMeter()
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
    private val dataSourceFactory = DefaultDataSourceFactory(getApplication() as Application, "com.ioraptor.exoplayerpoc", bandwidthMeter)
    private val videoSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("http://jell.yfish.us/media/jellyfish-3-mbps-hd-h264.mkv"))

    val player: ExoPlayer = buildPlayer()

    private fun buildPlayer(): ExoPlayer {

        // Create the player
        val player = ExoPlayerFactory.newSimpleInstance(getApplication() as Application, trackSelector)

        // Prepare the player with the source
        player.prepare(videoSource)

        // Play the video
        player.playWhenReady = true

        return player

    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

}