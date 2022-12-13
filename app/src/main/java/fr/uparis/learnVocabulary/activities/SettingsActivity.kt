package fr.uparis.learnVocabulary.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(getString(R.string.shared_preferences_name),Context.MODE_PRIVATE)

        val wordsPerSession = sharedPref.getInt(getString(R.string.number_of_words_per_session), 10).toString()
        binding.wordsPerSession.setText(wordsPerSession)
        binding.wordsPerSession.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                with(sharedPref.edit()) {
                    putInt(getString(R.string.number_of_words_per_session),binding.wordsPerSession.text.toString().toInt())
                    apply()
                }
            }
            false
        }

        val sessionsPerDay = sharedPref.getInt(getString(R.string.number_of_sessions_per_day),1).toString()
        binding.sessionsPerDay.setText(sessionsPerDay)
        binding.sessionsPerDay.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                with(sharedPref.edit()) {
                    putInt(getString(R.string.number_of_sessions_per_day),binding.sessionsPerDay.text.toString().toInt())
                    apply()
                }
            }
            false
        }

    }
}