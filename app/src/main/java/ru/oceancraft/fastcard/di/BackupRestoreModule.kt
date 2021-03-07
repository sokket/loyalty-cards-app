package ru.oceancraft.fastcard.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.oceancraft.fastcard.model.CardDao
import ru.oceancraft.fastcard.presentation.BackupRestorePM

@Module
class BackupRestoreModule {

    @Provides
    @FragmentScope
    fun pm(dao: CardDao, context: Context) = BackupRestorePM(dao, context)

}