package ru.oceancraft.fastcard

import ru.oceancraft.fastcard.di.AppComponent
import ru.oceancraft.fastcard.di.AppModule
import ru.oceancraft.fastcard.di.DaggerAppComponent
import ru.oceancraft.fastcard.ui.*

object Dagger {
    private lateinit var appComponent: AppComponent

    fun init(app: App) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app.applicationContext))
            .build()
    }

    fun inject(mainActivity: MainActivity) {
        appComponent.inject(mainActivity)
    }

    fun inject(cardsFragment: CardsFragment) {
        appComponent.cardsComponent().inject(cardsFragment)
    }

    fun inject(newCardFragment: NewCardFragment) {
        appComponent.newCardComponent().inject(newCardFragment)
    }

    fun inject(scanFragment: ScanFragment) {
        appComponent.scanComponent().inject(scanFragment)
    }

    fun inject(viewFragment: ViewFragment) {
        appComponent.cardViewComponent().inject(viewFragment)
    }

    fun inject(backupRestoreFragment: BackupRestoreFragment) {
        appComponent.backupRestoreComponent().inject(backupRestoreFragment)
    }

}