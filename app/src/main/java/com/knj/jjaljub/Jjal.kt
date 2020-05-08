package com.knj.jjaljub

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jjal")
class Jjal(@PrimaryKey(autoGenerate = true) var id: Int?,
           @ColumnInfo(name = "path") var path: String,
           @ColumnInfo(name = "tag") var tag: String
)