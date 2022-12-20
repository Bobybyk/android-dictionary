package fr.uparis.learnVocabulary.database

import androidx.room.*
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language

@Dao
interface VocabularyDAO {

    // Languages
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLanguage(vararg lang: Language) : List<Long>

    @Query("SELECT * FROM Language")
    fun loadAllLanguages() : List<Language>

    @Delete
    fun deleteLanguage(vararg lang: Language) : Int

    // Dictionaries
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertDictionary(vararg dico: Dictionary) : List<Long>

    @Query("SELECT * FROM Dictionary")
    fun loadAllDictionaries() : List<Dictionary>

    @Delete
    fun deleteDictionary(vararg dico : Dictionary) : Int
}