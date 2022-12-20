package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.ActivityReceiveDictionaryBinding
import fr.uparis.learnVocabulary.viewModels.ManageLanguagesViewModel
import fr.uparis.learnVocabulary.viewModels.ReceiveDictionaryViewModel

class ReceiveDictionaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReceiveDictionaryBinding
    private lateinit var url : String

    val model by lazy {
        ViewModelProvider(this)[ReceiveDictionaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.action.equals( "android.intent.action.SEND" )){
            url = intent.extras!!.getString( "android.intent.extra.TEXT" ).toString()
        }

        binding.receivedURL.text = url

        //update list after dictionary insert query
        model.insertInfo.observe(this) {
            if(it.equals(0))
                return@observe
            else if(it.equals(-1))
                notifyError()
            model.loadAllDictionaries()
        }

        //update list after deleting dictionary
        model.deleteInfo.observe(this) {
            if(it.equals(0))
                return@observe
            else if(it.equals(-1))
                notifyError()
            model.loadAllDictionaries()
        }
    }

    fun cutRoutes(url: String): List<String> {
        val strArray = mutableListOf<String>()
        return url.split(':')
    }

    fun notifyError() {
        Toast.makeText(this,"Une erreur est survenue !", Toast.LENGTH_SHORT).show()
    }
}