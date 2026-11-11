package com.nourfadaa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children")
data class ChildEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val ageYears: Int,
    val stars: Int = 0,
    val xp: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val lastActiveEpochDay: Long = -1L
)

@Entity(tableName = "lesson_progress", primaryKeys = ["childId", "lessonId"])
data class LessonProgressEntity(
    val childId: Long,
    val lessonId: String,
    val stars: Int,
    val completedEpochDay: Long
)
