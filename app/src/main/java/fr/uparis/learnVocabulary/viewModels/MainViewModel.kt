package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import kotlin.concurrent.thread

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = (application as LearnVocabularyApplication).database.getDAO()

    var langLoadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            langLoadInfo.postValue(dao.loadAllLanguages())
        }
    }

    var dicoAllLoadInfo = MutableLiveData<List<Dictionary>>(emptyList())
    fun loadAllDictionaries() {
        thread {
            dicoAllLoadInfo.postValue(dao.loadAllDictionaries())
        }
    }

    var dicoLoadInfo = MutableLiveData<List<Dictionary>>(emptyList())

    fun loadDicoParams(src: String, dst: String) {
        thread {
            dicoLoadInfo.postValue(dao.loadDictionaryParams(src, dst))
        }
    }

}