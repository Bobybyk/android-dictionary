package fr.uparis.pcm7

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import fr.uparis.pcm7.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("PCM7",Context.MODE_PRIVATE)

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