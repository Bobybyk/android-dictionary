package fr.uparis.learnVocabulary.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.uparis.learnVocabulary.LearnVocabularyApplication

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val dao = (application as LearnVocabularyApplication).database.getDAO()

}