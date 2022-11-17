package fr.uparis.learnVocabulary.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import fr.uparis.learnVocabulary.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("fr.uparis.learnVocabulary",Context.MODE_PRIVATE)

        val wordsPerSession = sharedPref.getInt("wordsPerSession", 10).toString()
        binding.wordsPerSession.setText(wordsPerSession)
        binding.wordsPerSession.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                with(sharedPref.edit()) {
                    putInt("wordsPerSession",binding.wordsPerSession.text.toString().toInt())
                    apply()
                }
            }
            false
        }

        val sessionsPerDay = sharedPref.getInt("sessionsPerDay",1).toString()
        binding.sessionsPerDay.setText(sessionsPerDay)
        binding.sessionsPerDay.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                with(sharedPref.edit()) {
                    putInt("sessionsPerDay",binding.sessionsPerDay.text.toString().toInt())
                    apply()
                }
            }
            false
        }

    }
}