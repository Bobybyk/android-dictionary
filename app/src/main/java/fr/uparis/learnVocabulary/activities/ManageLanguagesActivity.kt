package fr.uparis.learnVocabulary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.learnVocabulary.LanguagesListRecyclerViewAdapter
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.ActivityManageLanguagesBinding
import fr.uparis.learnVocabulary.viewModels.ManageLanguagesViewModel

class ManageLanguagesActivity : AppCompatActivity() {

    lateinit var binding : ActivityManageLanguagesBinding
    lateinit var adapter : LanguagesListRecyclerViewAdapter
    val model by lazy {
        ViewModelProvider(this)[ManageLanguagesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //Load all languages when creating class
        model.loadAllLanguages()

        //insert new language in the db when typing enter
        binding.newLanguage.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.newLanguage.text.isNotEmpty()) {
                    var lang = Language(binding.newLanguage.text.toString())
                    model.insertLanguage(lang)
                    binding.newLanguage.text.clear()
                }
            }
                false
        }

        //update list after language load query
        model.loadInfo.observe(this) {
            if(it.toMutableList().isNotEmpty()) {
                adapter = LanguagesListRecyclerViewAdapter(it.toMutableList())
                binding.recyclerView.adapter = adapter
            }
        }

        //update list after language insert query
        model.insertInfo.observe(this) {
            if(it.equals(0))
                return@observe
            else if(it.equals(-1))
                notifyError()
            model.loadAllLanguages()
        }

        //update list after deleting language
        model.deleteInfo.observe(this) {
            if(it.equals(0))
                return@observe
            else if(it.equals(-1))
                notifyError()
            model.loadAllLanguages()
        }

        //insert new language when clicking on button
        binding.add.setOnClickListener {
            if(binding.newLanguage.text.isNotEmpty()) {
                var lang = Language(binding.newLanguage.text.toString())
                model.insertLanguage(lang)
                binding.newLanguage.text.clear()
            }
        }
    }

    fun notifyError() {
        Toast.makeText(this,"Une erreur est survenue !",Toast.LENGTH_SHORT).show()
    }
}