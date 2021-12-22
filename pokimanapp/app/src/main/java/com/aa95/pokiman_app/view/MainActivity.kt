package com.aa95.pokiman_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.databinding.ActivityMainBinding
import com.aa95.pokiman_app.viewmodel.MainViewModel
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.supportActionBar?.hide()
        init()
    }

    private fun init(){

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        render()
    }

    private fun render(){
        binding.viewModel = viewModel

        Glide.with(this )
            .load("https://i.pinimg.com/originals/2c/f7/85/2cf785c8694626fe3d341c53992abc00.jpg")
            .placeholder(R.drawable.hp_bar)
            .into(binding.pokemonImg1)

        viewModel.myPokemon.observe(this, Observer {
            if(it.currentHp <= 0){
                binding.pokemon1Info.hpBarView.layoutParams.width = 1
            }else{
                val percent = calculateHpBarPercent(it.currentHp, it.maxHp)
                binding.pokemon1Info.hpBarView.layoutParams.width = (HPBARWIDTH * percent).toInt()
            }
        })

        Glide.with(this )
            .load("http://pm1.narvii.com/6104/d8e08bd5c1ec886de7a58db6e17422992ff85490_00.jpg")
            .placeholder(R.drawable.hp_bar)
            .into(binding.pokemonImg2)

        viewModel.enemyPokemon.observe(this, Observer {
            if(it.currentHp <= 0){
                binding.pokemon2Info.hpBarView.layoutParams.width = 1
            }else{
                val percent = calculateHpBarPercent(it.currentHp, it.maxHp)
                binding.pokemon2Info.hpBarView.layoutParams.width = (HPBARWIDTH * percent).toInt()
            }
        })
    }

    private fun calculateHpBarPercent(currentHp: Int, maxHp: Int) : Float{
        return currentHp.toFloat() / maxHp.toFloat()
    }


    companion object{
        const val HPBARWIDTH = 660
    }
}