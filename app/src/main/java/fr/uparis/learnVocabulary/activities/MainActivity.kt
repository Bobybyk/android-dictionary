package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.ActivityMainBinding
import fr.uparis.learnVocabulary.viewModels.ManageLanguagesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val model by lazy {
        ViewModelProvider(this)[ManageLanguagesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.loadAllLanguages()

        //when the button is clicked, launche the setting activity
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
            val url = "https://www.larousse.fr/dictionnaires/francais-anglais/${binding.searchWord.text}"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse( url )
            startActivity( intent )
        }

        model.loadInfo.observe(this) { it ->
            var langs : List<String> = it.map { it.lang }
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lFrom.adapter = adapter
            binding.lTo.adapter = adapter
        }
    }

    override fun onRestart() {
        super.onRestart()
        model.loadAllLanguages()
    }
}