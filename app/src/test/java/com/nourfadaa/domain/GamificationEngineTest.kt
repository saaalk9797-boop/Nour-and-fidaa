package com.nourfadaa.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class GamificationEngineTest {
    @Test fun firstActivityStartsStreak() =
        assertEquals(1, GamificationEngine.updateStreak(0, -1L, 100L))

    @Test fun sameDayKeepsStreak() =
        assertEquals(3, GamificationEngine.updateStreak(3, 100L, 100L))

    @Test fun nextDayIncrements() =
        assertEquals(4, GamificationEngine.updateStreak(3, 100L, 101L))

    @Test fun oneMissedDayIsForgiven() =
        assertEquals(4, GamificationEngine.updateStreak(3, 100L, 102L))

    @Test fun longGapRestartsGently() =
        assertEquals(1, GamificationEngine.updateStreak(9, 100L, 110L))

    @Test fun levelsAdvanceWithXp() {
        assertEquals(1, GamificationEngine.level(0))
        assertEquals(2, GamificationEngine.level(GamificationEngine.XP_PER_LEVEL))
    }

    @Test fun levelTitleNeverOutOfBounds() {
        GamificationEngine.levelTitle(0)
        GamificationEngine.levelTitle(999)
    }
}
