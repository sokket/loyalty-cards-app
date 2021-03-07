package ru.oceancraft.fastcard.di

import dagger.Subcomponent
import ru.oceancraft.fastcard.ui.ScanFragment

@FragmentScope
@Subcomponent
interface ScanComponent {
    fun inject(scanFragment: ScanFragment)
}