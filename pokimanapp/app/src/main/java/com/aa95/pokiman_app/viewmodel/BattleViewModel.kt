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

    val enemyFeed: MutableLiveData<String> = MutableLiveData(null)
    val myFeed: MutableLiveData<String> = MutableLiveData(null)

    val myActionEvent: MutableLiveData<ActionEvent> = MutableLiveData()
    val enemyActionEvent: MutableLiveData<ActionEvent> = MutableLiveData()

    init {
        getPokemon()
        startTurn()
    }

    private fun getPokemon() {
        myPokemon.value = Pokemon("Pikachu", 100, 10, 10, 100)
        enemyPokemon.value = Pokemon("Raichu", 100, 10, 10, 100)
    }

    fun attackPokemon(attacked: Boolean) {
        if (attacked) {
            myActionEvent.value = ActionEvent.Action.GettingAttacked
            enemyActionEvent.value = ActionEvent.Action.Attacking(enemyPokemon.value?.attack ?: 0)
        } else {
            enemyActionEvent.value = ActionEvent.Action.GettingAttacked
            myActionEvent.value = ActionEvent.Action.Attacking(myPokemon.value?.attack ?: 0)
            endingMyTurn.value = true
        }
    }

    fun enemyTurn() {
        isMyTurn.value = false
        clearFeed()
        clearState()
        if (checkIfAlive()) {
            isEnemyTurn.value = true
            enemyPokemon.value?.let {
                attackPokemon(true)
            }
        }
    }


    fun startTurn() {
        clearFeed()
        clearState()
        if (checkIfAlive()) {
            endingMyTurn.value = false
            isMyTurn.value = true
            isEnemyTurn.value = false
        }
    }

    fun calculateMyHp(damage: Int) {
        myActionEvent.value = ActionEvent.Action.Attacked
        myPokemon.value?.let { myPokemon ->
            myPokemon.currentHp =
                if ((myPokemon.currentHp - damage) <= 0) 0 else myPokemon.currentHp - damage

            this.myPokemon.value = myPokemon
        }
    }

    fun calculateEnemyHp(damage: Int) {
        enemyActionEvent.value = ActionEvent.Action.Attacked
        enemyPokemon.value?.let { enemyPokemon ->
            enemyPokemon.currentHp =
                if ((enemyPokemon.currentHp - damage) <= 0) 0 else enemyPokemon.currentHp - damage

            this.enemyPokemon.value = enemyPokemon
        }

    }

    private fun checkIfAlive(): Boolean {
        if (myPokemon.value?.currentHp != 0 && enemyPokemon.value?.currentHp != 0) {
            return true
        } else {
            endBattle()
        }

        return false
    }

    private fun endBattle() {
        if (myPokemon.value?.currentHp == 0) {
            myActionEvent.value = ActionEvent.Action.Dying
            myFeed.value = "DEAD"
        } else if (enemyPokemon.value?.currentHp == 0) {
            enemyActionEvent.value = ActionEvent.Action.Dying
            enemyFeed.value = "DEAD"
        }
    }

    private fun clearFeed() {
        myFeed.value = null
        enemyFeed.value = null
    }

    private fun clearState() {
        myActionEvent.value = null
        enemyActionEvent.value = null
    }

    fun healPokemon(healed: Boolean, hp: Int) {
        if (healed) {
            endingMyTurn.value = true
            myActionEvent.value = ActionEvent.Action.Heal(hp)
        }else{
            enemyActionEvent.value = ActionEvent.Action.Heal(hp)
        }
    }

    fun heal(hp: Int){

        myPokemon.value?.let { myPokemon ->
            myPokemon.currentHp += hp
            this.myPokemon.value = myPokemon
        }
    }

    fun healEnemy(hp: Int){
        enemyPokemon.value?.let { enemyPokemon ->
            enemyPokemon.currentHp += hp
            this.enemyPokemon.value = enemyPokemon
        }
    }

}

sealed class ActionEvent {
    sealed class Action : ActionEvent() {
        data class Attacking(val damage: Int) : Action()
        object GettingAttacked : Action()
        data class Heal(val hp: Int) : Action()
        object Attacked : Action()
        object Dying : Action()
    }
}