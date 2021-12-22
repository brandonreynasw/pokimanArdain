package com.aa95.pokiman_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.databinding.ActivityMainBinding
import com.aa95.pokiman_app.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        viewModel = MainViewModel(application)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        render()
    }

    private fun render(){
        binding.viewModel = viewModel
        viewModel.myPokemon.observe(this, Observer {
            binding.pokemon1 = it
        })
        viewModel.enemyPokemon.observe(this, Observer {
            binding.pokemon2 = it
        })
    }
}