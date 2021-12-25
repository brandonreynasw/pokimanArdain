package com.aa95.pokiman_app.util

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.viewmodel.BattleViewModel
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String?) {
        if (url != null) {
            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.hp_bar)
                .into(imageView)
        }
    }

    @BindingAdapter("currentHp", "maxHp")
    @JvmStatic
    fun setHpCount(
        textView: TextView,
        currentHp: String?,
        maxHp: String?
    ) {
        if (currentHp != null && maxHp != null) {
            textView.text = textView.context.getString(R.string.hp_stats, currentHp, maxHp)
        }
    }

    @BindingAdapter("receivedDamage", "viewModel", "inflictedDamage")
    @JvmStatic
    fun animateDamage(
        imgView: ImageView,
        receivedDamage: Boolean?,
        viewModel: BattleViewModel?,
        inflictedDamage: Boolean?
    ) {
        if (inflictedDamage != null) {
            if (inflictedDamage) {
                val animShake =
                    if (viewModel?.isEnemyTurn?.value == true) AnimationUtils.loadAnimation(
                        imgView.context,
                        R.anim.shake_once_enemy
                    ) else AnimationUtils.loadAnimation(imgView.context, R.anim.shake_once)
                imgView.startAnimation(animShake)
                if (viewModel != null) {
                    animShake.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            if (viewModel.isEnemyTurn.value == true) {
                                viewModel.enemyFeed.value =
                                    "Tackle ${viewModel.enemyPokemon.value?.attack} DAMAGE"
                            } else if (viewModel.isMyTurn.value == true) {
                                viewModel.myFeed.value =
                                    "Tackle ${viewModel.myPokemon.value?.attack} DAMAGE"
                            }
                        }

                        override fun onAnimationEnd(p0: Animation?) {

                            if (viewModel.isEnemyTurn.value == true) {
                                viewModel.inflictedDamaged.value = null
                                viewModel.receivedDamaged.value = true
                                viewModel.myPokemon.value?.let { myPokemon ->
                                    viewModel.enemyPokemon.value?.let { enemyPokemon ->
                                        myPokemon.currentHp =
                                            if ((myPokemon.currentHp - enemyPokemon.attack) <= 0) 0 else myPokemon.currentHp - enemyPokemon.attack
                                    }
                                    viewModel.myPokemon.value = myPokemon
                                }
                            } else if (viewModel.isMyTurn.value == true) {
                                viewModel.enemyInflictedDamaged.value = null
                                viewModel.enemyReceivedDamaged.value = true
                                viewModel.enemyPokemon.value?.let { enemyPokemon ->
                                    viewModel.myPokemon.value?.let { myPokemon ->
                                        enemyPokemon.currentHp =
                                            if ((enemyPokemon.currentHp - myPokemon.attack) <= 0) 0 else enemyPokemon.currentHp - myPokemon.attack
                                    }
                                    viewModel.enemyPokemon.value = enemyPokemon
                                }
                            }
                        }

                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                    })

                }
            }
        }
        if (receivedDamage != null) {
            if (receivedDamage) {
                val animShake = AnimationUtils.loadAnimation(imgView.context, R.anim.shake)
                imgView.startAnimation(animShake)
                if (viewModel != null) {
                    animShake.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            //If the animation starts during our turn, end turn in order for onAnimationEnd to work good
                            if (viewModel.isMyTurn.value == true) {
                                viewModel.endTurn()

                            }
                        }

                        override fun onAnimationEnd(p0: Animation?) {

                            if (viewModel.isEnemyTurn.value == true) {
                                viewModel.startTurn()
                                viewModel.enemyFeed.value = null

                            } else if (viewModel.isMyTurn.value == false) { //We set this false during the animation start and start enemy turn
                                viewModel.enemyTurn()
                                viewModel.myFeed.value = null
                            }
                        }

                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                    })

                }
            }
        }
    }
}