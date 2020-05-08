package com.knj.jjaljub

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface JjalDao {
    @Query("SELECT * FROM jjal")
    fun getAll(): List<Jjal>

    @Insert(onConflict = REPLACE)
    fun insert(jjal: Jjal)

    @Delete
    fun delete(jjal: Jjal)
}