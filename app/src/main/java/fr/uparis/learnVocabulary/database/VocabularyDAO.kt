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

        //get the dictionaries for given souce and target languages
    @Query("SELECT * FROM Dictionary WHERE sourceLanguage = :src AND destinationLanguage= :dst")
    fun getDictionaries(src: String, dst: String) : List<Dictionary>

    @Delete
    fun deleteDictionary(vararg dico : Dictionary) : Int
}