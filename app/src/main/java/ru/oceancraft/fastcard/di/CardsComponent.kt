package ru.oceancraft.fastcard.di

import dagger.Subcomponent
import ru.oceancraft.fastcard.ui.CardsFragment

@FragmentScope
@Subcomponent(modules = [CardsModule::class])
interface CardsComponent {
    fun inject(cardsFragment: CardsFragment)
}