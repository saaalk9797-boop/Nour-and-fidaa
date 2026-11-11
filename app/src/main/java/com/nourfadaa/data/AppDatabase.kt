package com.nourfadaa.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ChildEntity::class, LessonProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun lessonProgressDao(): LessonProgressDao
}
