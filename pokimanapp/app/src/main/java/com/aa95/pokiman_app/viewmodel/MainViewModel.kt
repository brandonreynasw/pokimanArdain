package com.aa95.pokiman_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.aa95.pokiman_app.model.Pokemon

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val myPokemon: MutableLiveData<Pokemon> = MutableLiveData()
    val enemyPokemon: MutableLiveData<Pokemon> = MutableLiveData()

    init {
        getPokemon()
    }

    private fun getPokemon(){
        myPokemon.value = Pokemon("Pikachu", 100, 10, 10, 100)
        enemyPokemon.value = Pokemon("Raichu", 100, 10, 10, 100)
    }

    fun attackPokemon(attacked: Boolean, damage: Int){
        if(attacked){
            myPokemon.value.apply {
                this!!.currentHp = if(this.currentHp == 0) 0 else this.currentHp - damage
                myPokemon.value = this
            }
        }else{
            enemyPokemon.value.apply {
                this!!.currentHp = if(this.currentHp == 0) 0 else this.currentHp - damage
                enemyPokemon.value = this
            }
        }

    }
}