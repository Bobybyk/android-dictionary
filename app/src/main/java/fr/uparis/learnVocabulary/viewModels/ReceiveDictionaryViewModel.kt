package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.Word
import kotlin.concurrent.thread

class ReceiveDictionaryViewModel(application: Application) : AndroidViewModel(application) {
    val dao = (application as LearnVocabularyApplication).database.getDAO()

    var dictionaryInsertInfo = MutableLiveData<Int>(0)
    fun insertDictionary(vararg dico : Dictionary) {
        thread {
            val d = dao.insertDictionary(*dico)
            dictionaryInsertInfo.postValue(d.fold(0) {acc: Int, d: Long ->
                if (d >= 0) acc + 1 else acc
            })
        }
    }

    var langLoadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            langLoadInfo.postValue(dao.loadAllLanguages())
        }
    }

    var wordInsertInfo = MutableLiveData<Int>(0)
    fun insertWord(vararg word: Word) {
        thread {
            val list = dao.insertWord(*word)
            wordInsertInfo.postValue(list.fold(0) {acc: Int, d: Long ->
                if (d >= 0) acc + 1 else acc
            })
        }
    }
}