package com.ioraptor.exoplayerpoc.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes


class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val bandwidthMeter = DefaultBandwidthMeter()
    private val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
    private val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
    private val dataSourceFactory = DefaultDataSourceFactory(getApplication() as Application, "com.ioraptor.exoplayerpoc", bandwidthMeter)
    private val mediaSourceFactory = ExtractorMediaSource.Factory(dataSourceFactory)

    private val playbackParameters = PlaybackParameters(1f)

    val player: ExoPlayer = buildPlayer()

    private fun buildPlayer(): ExoPlayer {

        // Create the player
        val player = ExoPlayerFactory.newSimpleInstance(getApplication() as Application, trackSelector)

        // Prepare the player with the source
        player.prepare(getMediaSources())

        // Play the video
        player.playWhenReady = true

        // Always repeat
        player.repeatMode = Player.REPEAT_MODE_ALL

        // Playback speed
        player.playbackParameters = playbackParameters

        return player

    }

    private fun getMediaSources(): MediaSource {
        val jellyMediaSource = subtitledMediaSource()
        val legoMediaSourceB = mediaSourceFactory.createMediaSource(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"))
        return ConcatenatingMediaSource(jellyMediaSource, legoMediaSourceB)
    }

    private fun subtitledMediaSource(): MediaSource {

        val mediaSource = mediaSourceFactory.createMediaSource(Uri.parse("http://jell.yfish.us/media/jellyfish-3-mbps-hd-h264.mkv"))

        val subtitleFormat = Format.createTextSampleFormat(
                "sub",
                MimeTypes.APPLICATION_SUBRIP,
                Format.NO_VALUE,
                null
        )

        val subtitleSource = SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(
                Uri.parse("http://45.79.158.247/test.srt"),
                subtitleFormat,
                C.TIME_UNSET
        )

        return MergingMediaSource(mediaSource, subtitleSource)

    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

}