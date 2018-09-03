package com.ioraptor.exoplayerpoc.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class PlayerViewModel(application: Application): AndroidViewModel(application) {

    private val bandwidthMeter = DefaultBandwidthMeter()
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
    private val dataSourceFactory = DefaultDataSourceFactory(getApplication() as Application, "com.ioraptor.exoplayerpoc", bandwidthMeter)

    private val playbackParameters = PlaybackParameters(1f)

    val player: ExoPlayer = buildPlayer()

    private fun buildPlayer(): ExoPlayer {

        // Create the player
        val player = ExoPlayerFactory.newSimpleInstance(getApplication() as Application, trackSelector)

        // Prepare the player with the source
        player.prepare(createMediaSources())

        // Play the video
        player.playWhenReady = true

        // Always repeat
        player.repeatMode = Player.REPEAT_MODE_ALL

        // Playback speed
        player.playbackParameters = playbackParameters

        return player

    }

    private fun createMediaSources() : MediaSource {
        val videoSourceA = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("http://jell.yfish.us/media/jellyfish-3-mbps-hd-h264.mkv"))
        val videoSourceB = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"))
        return ConcatenatingMediaSource(videoSourceA, videoSourceB)
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

}