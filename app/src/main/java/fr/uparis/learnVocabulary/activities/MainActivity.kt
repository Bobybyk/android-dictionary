package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.databinding.ActivityMainBinding
import fr.uparis.learnVocabulary.services.LearningService
import fr.uparis.learnVocabulary.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val model by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //launch the learning service when the activity is created
        launchService()

        //when the button is clicked, launch the setting activity
        binding.settings.setOnClickListener {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.browseLang.setOnClickListener {
            val intent = Intent(this,ManageLanguagesActivity::class.java)
            startActivity(intent)
        }

        //Searches the word written in the edit text when the button is clicked
        binding.search.setOnClickListener {
            var url : String
            if(binding.dico.selectedItem == "Google") {     //if no dictionary has been added by the user
                url = "http://www.google.fr/search?q=traduction+${binding.lFrom.selectedItem}+${binding.lTo.selectedItem}+${binding.searchWord.text}"
            } else {
                val dicoURL = (model.dicoLoadInfo.value!!.first {
                    it.sourceLanguage == binding.lFrom.selectedItem &&
                            it.destinationLanguage == binding.lTo.selectedItem
                }).url      //get the url of the selected dictionary
                url = "$dicoURL/${binding.searchWord.text}"
            }

            //launch the browser
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse( url )
            startActivity( intent )
        }

        //load languages when creating the activity
        model.loadAllLanguages()
        //observer to display the languages available
        model.langLoadInfo.observe(this) { it ->
            val langs : List<String> = it.map { it.lang }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, langs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lFrom.adapter = adapter
            binding.lTo.adapter = adapter
        }

        //observers to display the dictionaries available
        model.dicoLoadInfo.observe(this) { it ->

            it.forEach { it2 ->
                Log.d(null, it2.toString())
            }

            val list : List<String> = if(it.isNotEmpty()) {
                it.map { it.url }
            } else {
                listOf("Google")
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dico.adapter = adapter
        }

        //listeners to update the spinner with the right dictionary list when selecting languages in the spinners
        binding.lFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                model.loadDicoParams(binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {TODO("Not yet implemented")}
        }

        binding.lTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                model.loadDicoParams(binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {TODO("Not yet implemented")}
        }
    }

    //action to execute when the user comes back to the activity
    override fun onRestart() {
        super.onRestart()
        model.loadAllLanguages()
        model.loadAllDictionaries()
    }

    private fun launchService() {
        val intent = Intent(this, LearningService::class.java).apply {
            action = "test"
        }
        applicationContext.startService(intent)
    }

    private fun loadDictionaries(src: String, dst: String) {

    }
}