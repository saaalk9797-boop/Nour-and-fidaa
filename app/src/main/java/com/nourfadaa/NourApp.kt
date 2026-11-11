package com.nourfadaa

import android.app.Application
import androidx.room.Room
import com.nourfadaa.data.AppDatabase
import com.nourfadaa.data.ChildRepository
import com.nourfadaa.data.ContentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NourApp : Application() {
    lateinit var repository: ChildRepository
        private set

    val content: ContentRepository by lazy { ContentRepository(this) }

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "nour.db").build()
        repository = ChildRepository(db)
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { repository.seedIfEmpty() }
    }
}
