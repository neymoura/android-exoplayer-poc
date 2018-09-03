package com.ioraptor.exoplayerpoc.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import com.ioraptor.exoplayerpoc.R
import com.ioraptor.exoplayerpoc.viewModel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_player.*


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
    }

    private fun setImmersiveMode(){
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

}
