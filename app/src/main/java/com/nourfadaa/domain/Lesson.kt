package com.nourfadaa.domain

data class Source(
    val kind: String,
    val text: String,
    val book: String,
    val number: String,
    val grade: String
)

data class Lesson(
    val id: String,
    val sectionId: String,
    val order: Int,
    val title: String,
    val emoji: String,
    val minAge: Int,
    val maxAge: Int,
    val story: String,
    val question: String,
    val options: List<String>,
    val answerIndex: Int,
    val parentNote: String,
    val sources: List<Source>
)
