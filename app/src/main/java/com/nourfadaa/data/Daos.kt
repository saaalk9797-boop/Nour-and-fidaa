package com.nourfadaa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {
    @Query("SELECT * FROM children ORDER BY id")
    fun observeAll(): Flow<List<ChildEntity>>

    @Query("SELECT * FROM children WHERE id = :id")
    fun observe(id: Long): Flow<ChildEntity?>

    @Query("SELECT * FROM children WHERE id = :id")
    suspend fun get(id: Long): ChildEntity?

    @Query("SELECT COUNT(*) FROM children")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(child: ChildEntity)

    @Update
    suspend fun update(child: ChildEntity)
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE childId = :childId")
    fun observeForChild(childId: Long): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE childId = :childId AND lessonId = :lessonId")
    suspend fun get(childId: Long, lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: LessonProgressEntity)
}
