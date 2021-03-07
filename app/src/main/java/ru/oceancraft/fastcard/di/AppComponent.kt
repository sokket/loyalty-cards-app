package ru.oceancraft.fastcard.di

import dagger.Component
import ru.oceancraft.fastcard.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        RoomModule::class
    ]
)
interface AppComponent {
    fun cardsComponent(): CardsComponent
    fun newCardComponent() : NewCardComponent
    fun scanComponent() : ScanComponent
    fun cardViewComponent() : CardViewComponent

    fun inject(mainActivity: MainActivity)
}