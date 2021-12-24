package com.aa95.pokiman_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aa95.pokiman_app.model.Pokemon

class MainViewModel(application: Application) : BaseViewModel(application) {

    val myPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val enemyPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val isMyTurn: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getPokemon()
        isMyTurn.value = true
    }

    private fun getPokemon() {
        myPokemon.value = Pokemon("Pikachu", 100, 10, 10, 100)
        enemyPokemon.value = Pokemon("Raichu", 100, 10, 10, 100)
    }

    fun attackPokemon(attacked: Boolean, damage: Int) {
        if (attacked) {
            myPokemon.value?.let {
                it.currentHp =
                    if ((it.currentHp - damage) <= 0) 0 else it.currentHp - damage
                myPokemon.value = it
                isMyTurn.value = true
            }
        } else {
            enemyPokemon.value?.let {
                it.currentHp = if ((it.currentHp - damage) <= 0) 0 else it.currentHp - damage
                enemyPokemon.value = it
                endTurn()
            }
        }
    }

    private fun enemyTurn() {

        enemyPokemon.value?.let {
            attackPokemon(true, it.attack)
        }

    }

    private fun endTurn(){

        isMyTurn.value = false
        enemyTurn()

    }

}