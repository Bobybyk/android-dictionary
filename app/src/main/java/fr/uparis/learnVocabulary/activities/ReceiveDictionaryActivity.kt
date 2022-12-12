package fr.uparis.learnVocabulary.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.uparis.learnVocabulary.databinding.ActivityReceiveDictionaryBinding

class ReceiveDictionaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReceiveDictionaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.action.equals( "android.intent.action.SEND" )){
            val txt = intent.extras?.getString( "android.intent.extra.TEXT" )
            if (txt != null) {
                Log.i("ReceiveDictionnaryActivity", txt)
            }
        }
    }
}