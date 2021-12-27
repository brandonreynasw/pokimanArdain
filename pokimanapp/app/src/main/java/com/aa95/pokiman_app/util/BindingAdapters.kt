package com.aa95.pokiman_app.util

import android.view.View
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

    @BindingAdapter("receivedDamage", "viewModel", "inflictedDamage", "dead")
    @JvmStatic
    fun animateDamage(
        imgView: ImageView,
        receivedDamage: Boolean?,
        viewModel: BattleViewModel?,
        inflictedDamage: Boolean?,
        dead: Boolean?
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
                                viewModel.calculateMyHp()
                            } else if (viewModel.isMyTurn.value == true) {
                                viewModel.calculateEnemyHp()
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

                            } else if (viewModel.isMyTurn.value == false) { //We set this false during the animation start and start enemy turn
                                viewModel.enemyTurn()
                            }
                        }

                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                    })

                }
            }
        }
        if (dead != null){
            if(dead){
                val animDead = AnimationUtils.loadAnimation(imgView.context, R.anim.slide_down)
                imgView.startAnimation(animDead)
                animDead.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        imgView.visibility = View.INVISIBLE
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                })
            }

        }
    }
}