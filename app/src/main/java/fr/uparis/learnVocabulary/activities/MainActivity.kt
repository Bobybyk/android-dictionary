package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.databinding.ActivityMainBinding
import fr.uparis.learnVocabulary.services.LearningService
import fr.uparis.learnVocabulary.viewModels.MainViewModel
import kotlin.collections.List
import kotlin.collections.forEach
import kotlin.collections.get
import kotlin.collections.hashMapOf
import kotlin.collections.map
import kotlin.collections.set
import kotlin.collections.toTypedArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val model by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var dictionaries = hashMapOf<String, Dictionary>()

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

        binding.browseWords?.setOnClickListener {
            val intent = Intent(this,ManageWordsActivity::class.java)
            startActivity(intent)
        }

        //Searches the word written in the edit text when the button is clicked
        binding.search.setOnClickListener {
            val url = if(binding.dico.selectedItem.toString() == "Google") {
                "${dictionaries["Google"]!!.url}+${binding.lFrom.selectedItem}+${binding.lTo.selectedItem}+${binding.searchWord.text}"
            } else {
                "${dictionaries[binding.dico.selectedItem]!!.url}/${binding.searchWord.text}"
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
        model.dicoLoadInfo.observe(this) {

            dictionaries.clear()
            it.forEach { it2 ->
                dictionaries[it2.url.split('/')[2]] = it2
            }
            dictionaries["Google"] = Dictionary("http://www.google.fr/search?q=traduction+", "*", "*")

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dictionaries.keys.toTypedArray())
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
    }

    private fun launchService() {
        val intent = Intent(this, LearningService::class.java).apply {
            action = "start"
        }
        applicationContext.startService(intent)
    }
}