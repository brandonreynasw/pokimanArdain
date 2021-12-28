package com.aa95.pokiman_app.dependencyinjection

import com.aa95.pokiman_app.MyApplication
import com.aa95.pokiman_app.model.Pokemon
import com.aa95.pokiman_app.view.BattleActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @Named("MyPokemon")
    fun provideMyPokemon() = Pokemon("Pikachu", 100,10,10,100)
    
    @Provides
    @Named("EnemyPokemon")
    fun provideEnemyPokemon() = Pokemon("Raichu", 100, 10, 10, 100)
}