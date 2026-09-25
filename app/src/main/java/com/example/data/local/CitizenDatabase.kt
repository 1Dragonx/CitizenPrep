package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MockTestAttempt
import com.example.data.model.Question
import com.example.data.model.QuestionProgress
import com.example.data.model.UserProfile

@Database(
    entities = [
        Question::class,
        QuestionProgress::class,
        MockTestAttempt::class,
        UserProfile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CitizenDatabase : RoomDatabase() {
    abstract fun citizenDao(): CitizenDao

    companion object {
        @Volatile
        private var INSTANCE: CitizenDatabase? = null

        fun getDatabase(context: Context): CitizenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CitizenDatabase::class.java,
                    "citizenprep_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
