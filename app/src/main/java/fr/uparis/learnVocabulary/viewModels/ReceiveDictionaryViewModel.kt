package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import kotlin.concurrent.thread

class ReceiveDictionaryViewModel(application: Application) : AndroidViewModel(application) {
    val dao = (application as LearnVocabularyApplication).database.getDAO()

    var loadInfo = MutableLiveData<List<Dictionary>>(emptyList())
    fun loadAllDictionaries() {
        thread {
            loadInfo.postValue(dao.loadAllDictionaries())
        }
    }

    var insertInfo = MutableLiveData<Int>(0)
    fun insertDictionary(vararg dico : Dictionary) {
        thread {
            val d = dao.insertDictionary(*dico)
            insertInfo.postValue(d.fold(0) {acc: Int, d: Long ->
                if (d >= 0) acc + 1 else acc
            })
        }
    }

    var deleteInfo = MutableLiveData<Int>(0)
    fun deleteDictionary(vararg dico : Dictionary) {
        thread {
            val d = dao.deleteDictionary(*dico)
            deleteInfo.postValue(d)
        }
    }

    var langLoadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            langLoadInfo.postValue(dao.loadAllLanguages())
        }
    }
}