package fr.uparis.learnVocabulary.database

import androidx.room.*
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.Word

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
    fun loadDictionaryParams(src: String, dst: String) : List<Dictionary>

    @Delete
    fun deleteDictionary(vararg dico : Dictionary) : Int

    //Words
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertWord(vararg word : Word) : List<Long>

    @Query("SELECT * FROM Word")
    fun loadAllWords() : List<Word>

    @Query("SELECT * FROM Word WHERE sourceLanguage = :src AND destinationLanguage = :dst")
    fun loadWordParams(src: String, dst: String) : List<Word>

    @Delete
    fun deleteWord(vararg word : Word) : Int
}