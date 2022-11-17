package fr.uparis.learnVocabulary.database

import androidx.room.*
import fr.uparis.learnVocabulary.database.entities.Language

@Dao
interface VocabularyDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLanguage(vararg lang: Language) : List<Long>

    @Query("SELECT * FROM Language")
    fun loadAllLanguages() : List<Language>

    @Delete
    fun deleteLanguage(lang: Language) : Int

}