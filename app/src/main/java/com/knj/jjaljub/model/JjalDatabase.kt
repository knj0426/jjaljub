package com.knj.jjaljub.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Jjal::class], version = 1)
abstract class JjalDatabase : RoomDatabase() {
    abstract fun jjalDao(): JjalDao

    companion object {
        private const val DB_NAME = "jjal.db"

        @Volatile
        private var INSTANCE: JjalDatabase? = null

        fun getInstance(context: Context): JjalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                JjalDatabase::class.java,
                DB_NAME
            ).addMigrations(MIGRATION_1_TO_2).build()

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("Not yet implemented")
            }
        }

    }
}