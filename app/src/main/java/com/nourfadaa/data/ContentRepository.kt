package com.nourfadaa.data

import android.content.Context
import com.nourfadaa.domain.Lesson
import com.nourfadaa.domain.Source
import org.json.JSONObject

/** يقرأ الدروس من ملفات JSON داخل assets/content (اسم الملف يبدأ باسم القسم). */
class ContentRepository(private val context: Context) {
    private val cache = mutableMapOf<String, List<Lesson>>()

    fun lessons(sectionId: String): List<Lesson> =
        cache.getOrPut(sectionId) { load(sectionId) }

    fun lesson(sectionId: String, lessonId: String): Lesson? =
        lessons(sectionId).firstOrNull { it.id == lessonId }

    private fun load(sectionId: String): List<Lesson> {
        val files = context.assets.list("content")
            ?.filter { it.startsWith("${sectionId}_") && it.endsWith(".json") }
            ?: emptyList()
        return files.flatMap { parse(sectionId, it) }.sortedBy { it.order }
    }

    private fun parse(sectionId: String, file: String): List<Lesson> {
        val text = context.assets.open("content/$file").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val arr = JSONObject(text).getJSONArray("lessons")
        return (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            val opts = o.getJSONArray("options")
            val srcs = o.optJSONArray("sources")
            Lesson(
                id = o.getString("id"),
                sectionId = sectionId,
                order = o.optInt("order", i),
                title = o.getString("title"),
                emoji = o.optString("emoji", "⭐"),
                minAge = o.optInt("minAge", 3),
                maxAge = o.optInt("maxAge", 6),
                story = o.getString("story"),
                question = o.getString("question"),
                options = (0 until opts.length()).map { opts.getString(it) },
                answerIndex = o.getInt("answer"),
                parentNote = o.optString("parentNote", ""),
                sources = if (srcs == null) emptyList() else (0 until srcs.length()).map { j ->
                    val s = srcs.getJSONObject(j)
                    Source(
                        kind = s.optString("kind"),
                        text = s.optString("text"),
                        book = s.optString("book"),
                        number = s.optString("number"),
                        grade = s.optString("grade")
                    )
                }
            )
        }
    }
}
