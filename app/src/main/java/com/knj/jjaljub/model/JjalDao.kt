package com.knj.jjaljub.model

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface JjalDao {
    @Query("SELECT * FROM jjal")
    fun getAll(): DataSource.Factory<Int, Jjal>

    @Query("SELECT * FROM jjal WHERE tag LIKE :keyword")
    fun get(keyword: String?): DataSource.Factory<Int, Jjal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(jjal: Jjal)

    @Delete
    fun delete(jjal: Jjal)
}