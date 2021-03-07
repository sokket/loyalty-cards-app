package ru.oceancraft.fastcard.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao() : CardDao
}