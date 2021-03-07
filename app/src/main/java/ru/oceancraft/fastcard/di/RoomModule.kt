package ru.oceancraft.fastcard.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.oceancraft.fastcard.model.AppDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun database(context : Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "cards-db")
            .build()

    @Provides
    @Singleton
    fun cardsDao(database: AppDatabase) = database.cardDao()

}