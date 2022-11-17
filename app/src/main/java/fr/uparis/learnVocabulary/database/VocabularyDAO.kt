package fr.uparis.learnVocabulary.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.Word

@Dao
interface VocabularyDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLanguage(vararg lang: Language) : List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertWord(word: Word, sLang: Language, dLang : Language) : List<Long>

}