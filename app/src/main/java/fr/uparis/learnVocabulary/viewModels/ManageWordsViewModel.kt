package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.database.entities.Word
import kotlin.concurrent.thread

class ManageWordsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = (application as LearnVocabularyApplication).database.getDAO()

    var langLoadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            langLoadInfo.postValue(dao.loadAllLanguages())
        }
    }

    var insertInfo = MutableLiveData(0)
    fun insertWord(vararg word : Word) {
        thread {
            val list = dao.insertWord(*word)
            insertInfo.postValue(list.fold(0) {acc: Int, l: Long ->
                if(l >= 0) acc + 1 else acc
            })
        }
    }

    var wordLoadInfo = MutableLiveData<List<Word>>(emptyList())
    fun loadAllWords() {
        thread {
            wordLoadInfo.postValue(dao.loadAllWords())
        }
    }
}