package com.shihab.kotlintoday.feature.mvvm.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shihab.kotlintoday.feature.mvvm.dao.NoteDao
import com.shihab.kotlintoday.feature.mvvm.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {

            if (instance == null) {

                instance = Room.databaseBuilder(
                    context, NoteDatabase::class.java, "note_db"
                ).addCallback(roomCallback).build()

            }
            return instance as NoteDatabase
        }

        private val roomCallback = object : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                instance?.let { PopulateDbAsyncTask(it).execute() }

            }
        }

    }

    private class PopulateDbAsyncTask(val db: NoteDatabase) :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg voids: Void?): Void? {
            db.noteDao().insertNote(Note(1, "Wake Up", "wake up early and pray...", "1"))
            db.noteDao().insertNote(
                Note(
                    2,
                    "Meeting with Tech Team",
                    "Important about upcoming sprint",
                    "2"
                )
            )
            db.noteDao().insertNote(Note(3, "Make an Mvvm Demo", "with kotlin and rx java", "3"))
            return null
        }
    }

    //https://www.youtube.com/watch?v=0cg09tlAAQ0&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118&index=3
    //https://proandroiddev.com/mvvm-with-kotlin-android-architecture-components-dagger-2-retrofit-and-rxandroid-1a4ebb38c699

}