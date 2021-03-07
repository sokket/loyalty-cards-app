package ru.oceancraft.fastcard.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CiceroneModule {

    @Provides
    @Singleton
    fun cicerone() = Cicerone.create()

    @Provides
    @Singleton
    fun router(cicerone: Cicerone<Router>) = cicerone.router

    @Provides
    @Singleton
    fun navigationHolder(cicerone : Cicerone<Router>) = cicerone.getNavigatorHolder()

}