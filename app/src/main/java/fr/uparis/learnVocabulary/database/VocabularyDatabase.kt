package fr.uparis.learnVocabulary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.Word

@Database(entities = [Language::class, Dictionary::class, Word::class], version = 6, exportSchema = false)
abstract class VocabularyDatabase : RoomDatabase(){

    abstract fun getDAO() : VocabularyDAO

    companion object {
        private var instance : VocabularyDatabase? = null
        fun getDatabase(context: Context) : VocabularyDatabase {
            if(instance != null) return instance!!

            val db = Room.databaseBuilder(context.applicationContext,VocabularyDatabase::class.java,"fr.uparis.learnVocabulary").build()
            instance = db
            return instance!!
        }
    }
}