package com.aa95.pokiman_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.databinding.ActivityMainBinding
import com.aa95.pokiman_app.viewmodel.MainViewModel

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
        viewModel.myPokemon.observe(this, Observer {
            binding.pokemon1 = it
        })
        viewModel.enemyPokemon.observe(this, Observer {
            binding.pokemon2 = it
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