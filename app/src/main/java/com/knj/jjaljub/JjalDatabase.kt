package com.knj.jjaljub

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Jjal::class], version = 1)
abstract class JjalDatabase : RoomDatabase() {
    abstract fun jjalDao() : JjalDao
}