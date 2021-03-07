package ru.oceancraft.fastcard.di

import dagger.Subcomponent
import ru.oceancraft.fastcard.ui.NewCardFragment

@FragmentScope
@Subcomponent(modules = [NewCardModule::class])
interface NewCardComponent {
    fun inject(newCardFragment: NewCardFragment)
}