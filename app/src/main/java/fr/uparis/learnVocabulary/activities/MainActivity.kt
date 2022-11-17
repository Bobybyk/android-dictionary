package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //Populate the spinners with the languages
        ArrayAdapter.createFromResource(this, R.array.languages,android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lFrom.adapter = adapter
        }
        ArrayAdapter.createFromResource(this, R.array.languages,android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lTo.adapter = adapter
        }
    }
}