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

    var dicoLoadInfo = MutableLiveData<List<Dictionary>>(emptyList())
    fun loadAllDictionaries() {
        thread {
            dicoLoadInfo.postValue(dao.loadAllDictionaries())
        }
    }

    fun loadDicoParams(src: String, dst: String) {
        thread {
            dicoLoadInfo.postValue(dao.getDictionaries(src, dst))
        }
    }

}