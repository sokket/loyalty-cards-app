package ru.oceancraft.fastcard.di

import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import ru.oceancraft.fastcard.model.CardDao
import ru.oceancraft.fastcard.presentation.CardsPM

@Module
class CardsModule {

    @Provides
    @FragmentScope
    fun pm(cardDao: CardDao, router: Router) = CardsPM(cardDao, router)

}