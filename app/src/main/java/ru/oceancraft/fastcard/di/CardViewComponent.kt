package ru.oceancraft.fastcard.di

import dagger.Subcomponent
import ru.oceancraft.fastcard.ui.ViewFragment

@FragmentScope
@Subcomponent(modules = [CardViewModule::class])
interface CardViewComponent {
    fun inject(viewFragment: ViewFragment)
}