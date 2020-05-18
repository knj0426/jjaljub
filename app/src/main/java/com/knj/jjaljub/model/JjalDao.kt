package com.knj.jjaljub.model

import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.knj.jjaljub.model.Jjal

@Dao
interface JjalDao {
    @Query("SELECT * FROM jjal")
    fun getAll(): DataSource.Factory<Int, Jjal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(jjal: Jjal)

    @Delete
    fun delete(jjal: Jjal)
}