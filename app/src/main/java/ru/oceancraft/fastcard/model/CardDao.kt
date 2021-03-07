package ru.oceancraft.fastcard.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CardDao {

    @Insert
    fun insert(card: Card): Single<Long>

    @Query("SELECT * FROM Card")
    fun load(): Single<List<Card>>

    @Query("SELECT * FROM Card WHERE id = :id")
    fun getById(id : Long) : Single<Card>

    @Query("DELETE FROM Card WHERE id = :id")
    fun delete(id : Long) : Completable
}