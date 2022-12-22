package fr.uparis.learnVocabulary.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.database.entities.Word
import fr.uparis.learnVocabulary.databinding.ActivityReceiveDictionaryBinding
import fr.uparis.learnVocabulary.viewModels.ReceiveDictionaryViewModel

class ReceiveDictionaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReceiveDictionaryBinding
    private lateinit var url : String

    private val model by lazy {
        ViewModelProvider(this)[ReceiveDictionaryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val langs : List<String> = it.map { it2 -> it2.lang }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, langs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lFrom.adapter = adapter
            binding.lTo.adapter = adapter
        }

        //add the dictionary to the database when clicking the button
        binding.add.setOnClickListener {
            model.insertDictionary(Dictionary(url.substring(0,url.lastIndexOf('/')), binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString()))
            model.insertWord(Word(binding.newWord.text.toString(), binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString()))
            finish()
        }
    }
}