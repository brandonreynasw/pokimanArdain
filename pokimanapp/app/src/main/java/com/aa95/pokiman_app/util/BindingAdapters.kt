package com.aa95.pokiman_app.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aa95.pokiman_app.R
import com.aa95.pokiman_app.viewmodel.ActionEvent
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

    @BindingAdapter( "viewModel",  "action")
    @JvmStatic
    fun animateDamage(
        imgView: ImageView,
        viewModel: BattleViewModel?,
        action: ActionEvent?
    ) {

        if (action != null) {
            when (action) {
                is ActionEvent.Action.Heal -> {
                    val animShake = AnimationUtils.loadAnimation(imgView.context, R.anim.shake)
                    imgView.startAnimation(animShake)
                    if (viewModel != null) {
                        animShake.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {
                                //If the animation starts during our turn, end turn in order for onAnimationEnd to work good
                                if (viewModel.isMyTurn.value == true) {
                                    viewModel.endTurn()
                                    viewModel.myFeed.value =
                                        "Heal ${action.hp} hp"
                                }
                                else{
                                    viewModel.enemyFeed.value =
                                        "Heal ${action.hp} hp"
                                }
                            }

                            override fun onAnimationEnd(p0: Animation?) {

                                if (viewModel.isEnemyTurn.value == true) {
                                    viewModel.healEnemy(action.hp)

                                    viewModel.startTurn()
                                } else if (viewModel.isMyTurn.value == false) { //We set this false during the animation start and start enemy turn
                                    viewModel.heal(action.hp)
                                    viewModel.enemyTurn()
                                }
                            }

                            override fun onAnimationRepeat(p0: Animation?) {
                            }

                        })

                    }
                }
                is ActionEvent.Action.Attacked -> {
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
                is ActionEvent.Action.Attacking -> {
                    val animShake = AnimationUtils.loadAnimation(imgView.context, R.anim.shake_once)
                    imgView.startAnimation(animShake)

                    if (viewModel != null) {
                        animShake.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(p0: Animation?) {

                                if (viewModel.isEnemyTurn.value == true){
                                    viewModel.enemyFeed.value =
                                        "Tackle ${action.damage} DAMAGE"
                                }
                                if (viewModel.isMyTurn.value == true){
                                    viewModel.myFeed.value =
                                        "Tackle ${action.damage} DAMAGE"
                                }
                            }

                            override fun onAnimationEnd(p0: Animation?) {
                                if (viewModel.isEnemyTurn.value == true){
                                    viewModel.calculateMyHp(action.damage)
                                }
                                if (viewModel.isMyTurn.value == true){
                                    viewModel.calculateEnemyHp(action.damage)
                                }
                            }

                            override fun onAnimationRepeat(p0: Animation?) {
                            }

                        })
                    }
                }
                is ActionEvent.Action.GettingAttacked -> {
                }
                is ActionEvent.Action.Dying ->{
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
}