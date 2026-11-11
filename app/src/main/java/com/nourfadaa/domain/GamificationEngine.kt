package com.nourfadaa.domain

import kotlin.math.max
import kotlin.math.min

data class LessonReward(
    val starsEarned: Int,
    val xpEarned: Int,
    val leveledUp: Boolean,
    val newLevel: Int,
    val streak: Int,
    val alreadyCompleted: Boolean
) {
    companion object {
        val NONE = LessonReward(0, 0, false, 1, 0, false)
    }
}

object GamificationEngine {
    const val XP_PER_STAR = 10
    const val XP_PER_LEVEL = 50

    private val titles = listOf(
        "🌱 بذرة", "🌿 برعم", "🌷 زهرة", "🌳 شجرة", "🍎 شجرة مثمرة", "🌟 نجمة النور"
    )

    fun level(xp: Int): Int = xp / XP_PER_LEVEL + 1
    fun levelProgress(xp: Int): Float = (xp % XP_PER_LEVEL) / XP_PER_LEVEL.toFloat()
    fun levelTitle(level: Int): String = titles[min(max(level, 1), titles.size) - 1]

    fun updateStreak(current: Int, lastActiveEpochDay: Long, today: Long): Int {
        if (lastActiveEpochDay < 0) return 1
        val gap = today - lastActiveEpochDay
        return when {
            gap <= 0L -> max(current, 1)
            gap <= 2L -> current + 1
            else -> 1
        }
    }
}
