package fr.uparis.learnVocabulary.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ArrayAdapter
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

        //display the received URL
        binding.receivedURL.text = url

        //load the existing languages to select dictionary source and destination languages
        model.loadAllLanguages()
        model.langLoadInfo.observe(this) {
            val langs : List<String> = it.map { it.lang }
            val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, langs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lFrom.adapter = adapter
            binding.lTo.adapter = adapter
        }

        //add the dictionary to the datbase when clicking the button
        binding.add.setOnClickListener {
            model.insertDictionary(Dictionary(url, binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString()))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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