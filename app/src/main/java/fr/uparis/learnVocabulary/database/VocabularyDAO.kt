package fr.uparis.learnVocabulary.database

import androidx.room.*
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.LanguageInfo

@Dao
interface VocabularyDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLanguage(vararg lang: Language) : List<Long>

    @Query("SELECT * FROM Language")
    fun loadAllLanguages() : List<Language>

    @Delete(entity = Language::class)
    fun deleteLanguage(lang: LanguageInfo) : Int

}