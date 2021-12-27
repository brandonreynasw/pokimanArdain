package com.aa95.pokiman_app.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aa95.pokiman_app.model.Pokemon

class BattleViewModel(application: Application) : BaseViewModel(application) {

    val myPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val enemyPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val isMyTurn: MutableLiveData<Boolean> = MutableLiveData()
    val endingMyTurn: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEnemyTurn: MutableLiveData<Boolean> = MutableLiveData()
    val receivedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val enemyReceivedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val inflictedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val enemyInflictedDamaged: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDead: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEnemyDead: MutableLiveData<Boolean> = MutableLiveData(false)
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
            endingMyTurn.value = true
            inflictedDamaged.value = true
        }
    }

    fun enemyTurn() {
        clearFeed()
        if (checkIfAlive()){
            isEnemyTurn.value = true
            enemyPokemon.value?.let {
                attackPokemon(true, it.attack)
            }
        }
    }

    fun endTurn() {
        isMyTurn.value = false
        enemyReceivedDamaged.value = null
    }

    fun startTurn() {
        clearFeed()
        if (checkIfAlive()) {
            endingMyTurn.value = false
            isMyTurn.value = true
            isEnemyTurn.value = false
            receivedDamaged.value = null
        }
    }

    fun calculateMyHp() {
        inflictedDamaged.value = null
        receivedDamaged.value = true
        myPokemon.value?.let { myPokemon ->
            enemyPokemon.value?.let { enemyPokemon ->
                myPokemon.currentHp =
                    if ((myPokemon.currentHp - enemyPokemon.attack) <= 0) 0 else myPokemon.currentHp - enemyPokemon.attack
            }
            this.myPokemon.value = myPokemon
        }
    }

    fun calculateEnemyHp() {
        enemyInflictedDamaged.value = null
        enemyReceivedDamaged.value = true
        enemyPokemon.value?.let { enemyPokemon ->
            myPokemon.value?.let { myPokemon ->
                enemyPokemon.currentHp =
                    if ((enemyPokemon.currentHp - myPokemon.attack) <= 0) 0 else enemyPokemon.currentHp - myPokemon.attack
            }
            this.enemyPokemon.value = enemyPokemon
        }
    }

    private fun checkIfAlive(): Boolean
    {
        if (myPokemon.value?.currentHp != 0 && enemyPokemon.value?.currentHp != 0) {
            return true
        }
        else{
            endBattle()
        }

        return false
    }

    private fun endBattle(){
        if(myPokemon.value?.currentHp == 0){
            isDead.value = true
            myFeed.value = "DEAD"
        }else if(enemyPokemon.value?.currentHp == 0){
            isEnemyDead.value = true
            enemyFeed.value = "DEAD"
        }
    }

    private fun clearFeed(){
        myFeed.value = null
        enemyFeed.value = null
    }
}

enum class Actions {
    ATTACK
}