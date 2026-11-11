package com.nourfadaa.domain

enum class AgeBand {
    TODDLER,
    PRESCHOOL;

    companion object {
        fun fromAge(age: Int): AgeBand = if (age <= 4) TODDLER else PRESCHOOL
    }
}

enum class Section(val id: String, val emoji: String, val title: String) {
    ESSENTIALS("essentials", "🧭", "ما لا يسع الطفل المسلم جهله"),
    QURAN("quran", "📖", "قرآني"),
    PRAYER("prayer", "🕌", "صلاتي"),
    ADHKAR("adhkar", "🌙", "أذكاري"),
    AKHLAQ("akhlaq", "❤️", "أخلاقي الجميلة"),
    SEERAH("seerah", "📚", "سيرة نبينا ﷺ"),
    FIGURES("figures", "⭐", "شخصيات أحبها"),
    PROPHETS("prophets", "🌿", "قصص الأنبياء"),
    LEARN("learn", "🧠", "أتعلم"),
    ACHIEVEMENTS("achievements", "🌟", "إنجازاتي");

    companion object {
        fun fromId(id: String): Section = values().first { it.id == id }
    }
}
