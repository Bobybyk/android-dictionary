package fr.uparis.learnVocabulary

import android.app.Application
import fr.uparis.learnVocabulary.database.VocabularyDatabase

class LearnVocabularyApplication : Application() {
    val database by lazy {
        VocabularyDatabase.getDatabase(this)
    }
}