package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Language
import kotlin.concurrent.thread

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = (application as LearnVocabularyApplication).database.getDAO()

    var loadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            loadInfo.postValue(dao.loadAllLanguages())
        }
    }

}