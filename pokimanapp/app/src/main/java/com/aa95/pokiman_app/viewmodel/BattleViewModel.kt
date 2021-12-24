package com.aa95.pokiman_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aa95.pokiman_app.model.Pokemon

class BattleViewModel(application: Application) : BaseViewModel(application) {

    val myPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val enemyPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val isMyTurn: MutableLiveData<Boolean> = MutableLiveData()
    val isEnemyTurn: MutableLiveData<Boolean> = MutableLiveData()
    val receivedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val enemyReceivedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val inflictedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val enemyInflictedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val enemyFeed: MutableLiveData<String> = MutableLiveData(null)
    val myFeed: MutableLiveData<String> = MutableLiveData(null)

    init {
        getPokemon()
        startTurn()
    }

    private fun getPokemon() {
        myPokemon.value = Pokemon("Pikachu", 100, 10, 10, 100)
        enemyPokemon.value = Pokemon("Raichu", 100, 10, 10, 100)
    }

    fun attackPokemon(attacked: Boolean, damage: Int) {
        if (attacked) {
            enemyInflictedDamaged.value = true
        } else {
            inflictedDamaged.value = true
        }
    }

    fun enemyTurn() {
        isEnemyTurn.value = true
        enemyPokemon.value?.let {
            attackPokemon(true, it.attack)
        }
    }

    fun endTurn() {
        isMyTurn.value = false
        enemyReceivedDamaged.value = null
    }

    fun startTurn() {
        isMyTurn.value = true
        isEnemyTurn.value = false
        receivedDamaged.value = null
    }

}

enum class Actions {
    ATTACK
}