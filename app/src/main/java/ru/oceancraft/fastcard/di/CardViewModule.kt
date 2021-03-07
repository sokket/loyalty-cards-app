package ru.oceancraft.fastcard.di

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.oceancraft.fastcard.model.CardDao
import ru.oceancraft.fastcard.presentation.CardViewPM

@Module
class CardViewModule {

    @Provides
    fun pm(router: Router, dao: CardDao) = CardViewPM(dao, router)

}