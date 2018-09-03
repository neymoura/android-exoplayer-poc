package com.ioraptor.exoplayerpoc.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ioraptor.exoplayerpoc.R
import com.ioraptor.exoplayerpoc.viewModel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)

        setup()

    }

    private fun setup() {
        exo_play.player = viewModel.player
    }

}
