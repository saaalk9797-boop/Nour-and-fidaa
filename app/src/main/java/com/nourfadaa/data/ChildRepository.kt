package com.nourfadaa.data

import androidx.room.withTransaction
import com.nourfadaa.domain.GamificationEngine
import com.nourfadaa.domain.LessonReward
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import kotlin.math.max

class ChildRepository(private val db: AppDatabase) {
    private val children = db.childDao()
    private val progress = db.lessonProgressDao()

    fun observeChildren(): Flow<List<ChildEntity>> = children.observeAll()
    fun observeChild(id: Long): Flow<ChildEntity?> = children.observe(id)
    fun observeProgress(childId: Long): Flow<List<LessonProgressEntity>> =
        progress.observeForChild(childId)

    suspend fun seedIfEmpty() {
        if (children.count() == 0) {
            children.insert(ChildEntity(id = 1, name = "فداء", ageYears = 5))
            children.insert(ChildEntity(id = 2, name = "نور", ageYears = 3))
        }
    }

    suspend fun completeLesson(
        childId: Long,
        lessonId: String,
        stars: Int = 3,
        today: Long = LocalDate.now().toEpochDay()
    ): LessonReward = db.withTransaction {
        val child = children.get(childId) ?: return@withTransaction LessonReward.NONE
        val already = progress.get(childId, lessonId) != null
        val earnedStars = if (already) 0 else stars.coerceIn(1, 3)
        val earnedXp = earnedStars * GamificationEngine.XP_PER_STAR
        val newStreak = GamificationEngine.updateStreak(child.streak, child.lastActiveEpochDay, today)
        val oldLevel = GamificationEngine.level(child.xp)

        val updated = child.copy(
            stars = child.stars + earnedStars,
            xp = child.xp + earnedXp,
            streak = newStreak,
            bestStreak = max(child.bestStreak, newStreak),
            lastActiveEpochDay = today
        )
        children.update(updated)
        if (!already) progress.upsert(LessonProgressEntity(childId, lessonId, earnedStars, today))

        val newLevel = GamificationEngine.level(updated.xp)
        LessonReward(
            starsEarned = earnedStars,
            xpEarned = earnedXp,
            leveledUp = newLevel > oldLevel,
            newLevel = newLevel,
            streak = newStreak,
            alreadyCompleted = already
        )
    }
}
