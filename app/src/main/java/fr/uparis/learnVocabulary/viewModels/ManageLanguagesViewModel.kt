package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.database.entities.Language
import kotlin.concurrent.thread

class ManageLanguagesViewModel (application: Application) : AndroidViewModel(application) {

    private val dao = (application as LearnVocabularyApplication).database.getDAO()

    var loadInfo = MutableLiveData<List<Language>>(emptyList())
    fun loadAllLanguages() {
        thread {
            loadInfo.postValue(dao.loadAllLanguages())
        }
    }

    var insertInfo = MutableLiveData(0)
    fun insertLanguage(vararg lang: Language) {
        thread {
            val list = dao.insertLanguage(*lang)
            insertInfo.postValue(list.fold(0) {acc: Int, l: Long ->
                if(l >= 0) acc + 1 else acc
            })
        }
    }

    var deleteInfo = MutableLiveData(0)
    fun deleteLanguage(vararg lang : Language) {
        thread {
            val l = dao.deleteLanguage(*lang)
            deleteInfo.postValue(l)
        }
    }
}