package com.ioraptor.exoplayerpoc.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.TextureView
import android.view.View.*
import com.amazonaws.services.rekognition.model.DetectLabelsRequest
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.ioraptor.exoplayerpoc.R
import com.ioraptor.exoplayerpoc.aws.ClientFactory
import com.ioraptor.exoplayerpoc.viewModel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setImmersiveMode()
        setup()
    }

    private fun setup() {
        viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        player_view.player = viewModel.player

        viewModel.player.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            }

            override fun onSeekProcessed() {
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {
            }

            override fun onPositionDiscontinuity(reason: Int) {
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    // media actually playing
                } else if (playWhenReady) {
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)
                } else {
                    // player paused in any state
                    triggerRekognition()
                }
            }
        })

    }

    private fun triggerRekognition() {
        CoroutineScope(Dispatchers.IO).launch{
            val pausedFrame = (player_view.videoSurfaceView as TextureView).bitmap
            reckognition(pausedFrame)
        }
    }

    private fun setImmersiveMode(){
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun reckognition(input: Bitmap){

        //Image
        val stream = ByteArrayOutputStream()
        input.compress(Bitmap.CompressFormat.PNG, 10, stream)
        val image = Image().withBytes(ByteBuffer.wrap(stream.toByteArray()))

        //Rekognition Client
        val rekognition = ClientFactory.createClient()

        //Labels Request
//        val labelsRequest = DetectLabelsRequest().withImage(image)
//        val labelsResult = rekognition.detectLabels(labelsRequest)
//        val labels = labelsResult.labels.map { it.name }.joinToString()
//        Snackbar.make(root_view, labels, Snackbar.LENGTH_LONG).show()

        //Celebrities Request
        val request = RecognizeCelebritiesRequest().withImage(image)
        val celebritiesNames = rekognition.recognizeCelebrities(request).celebrityFaces.map { it.name }.joinToString()
        Snackbar.make(root_view, celebritiesNames, Snackbar.LENGTH_LONG).show()

    }

}
