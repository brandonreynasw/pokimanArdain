package com.aa95.pokiman_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.databinding.ActivityBattleBinding
import com.aa95.pokiman_app.viewmodel.BattleViewModel

class BattleActivity : AppCompatActivity() {

    private val viewModel: BattleViewModel by viewModels()
    private lateinit var binding: ActivityBattleBinding
    private var hpBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        this.supportActionBar?.hide()
        init()
    }

    private fun init(){

        binding = DataBindingUtil.setContentView(this, R.layout.activity_battle)
        binding.lifecycleOwner = this
        hpBarSize = binding.pokemon1Info.hpBarView.layoutParams.width
        render()
    }

    private fun render(){
        binding.viewModel = viewModel

        viewModel.myPokemon.observe(this, Observer {
            if(it.currentHp <= 0){
                binding.pokemon1Info.hpBarView.layoutParams.width = 1
            }else{
                val percent = calculateHpBarPercent(it.currentHp, it.maxHp)
                binding.pokemon1Info.hpBarView.layoutParams.width = (hpBarSize * percent).toInt()
            }
        })
        viewModel.enemyPokemon.observe(this, Observer {
            if(it.currentHp <= 0){
                binding.pokemon2Info.hpBarView.layoutParams.width = 1
            }else{
                val percent = calculateHpBarPercent(it.currentHp, it.maxHp)
                binding.pokemon2Info.hpBarView.layoutParams.width = (hpBarSize * percent).toInt()
            }
        })
    }

    private fun calculateHpBarPercent(currentHp: Int, maxHp: Int) : Float{
        return currentHp.toFloat() / maxHp.toFloat()
    }

}