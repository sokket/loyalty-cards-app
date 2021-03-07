package ru.oceancraft.fastcard.di

import dagger.Subcomponent
import ru.oceancraft.fastcard.ui.BackupRestoreFragment

@FragmentScope
@Subcomponent(modules = [BackupRestoreModule::class])
interface BackupRestoreComponent {
    fun inject(backupRestoreFragment: BackupRestoreFragment)
}