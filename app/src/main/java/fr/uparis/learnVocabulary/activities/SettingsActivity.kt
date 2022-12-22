package fr.uparis.learnVocabulary.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(getString(R.string.shared_preferences_name),Context.MODE_PRIVATE)

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

        with(binding.startTime) {
            setIs24HourView(true)
            hour = sharedPref.getInt(getString(R.string.training_start_hour), 8)
            minute = sharedPref.getInt(getString(R.string.training_start_minutes), 0)
        }

        with(binding.stopTime) {
            setIs24HourView(true)
            hour = sharedPref.getInt(getString(R.string.training_stop_hour), 8)
            minute = sharedPref.getInt(getString(R.string.training_stop_minutes), 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val startHour : Int = binding.startTime.hour
        val startMinutes : Int = binding.startTime.minute
        val stopHour : Int = binding.stopTime.hour
        val stopMinutes : Int = binding.stopTime.minute

        with(sharedPref.edit()) {
            putInt(getString(R.string.training_start_hour), startHour)
            putInt(getString(R.string.training_start_minutes), startMinutes)
            putInt(getString(R.string.training_stop_hour), stopHour)
            putInt(getString(R.string.training_stop_minutes), stopMinutes)
            apply()
        }
    }
}