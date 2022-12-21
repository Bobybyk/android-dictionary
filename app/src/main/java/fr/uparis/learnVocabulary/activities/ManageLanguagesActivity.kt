package fr.uparis.learnVocabulary.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.learnVocabulary.LanguagesListRecyclerViewAdapter
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.ActivityManageLanguagesBinding
import fr.uparis.learnVocabulary.viewModels.ManageLanguagesViewModel

class ManageLanguagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityManageLanguagesBinding
    private lateinit var adapter : LanguagesListRecyclerViewAdapter
    private val model by lazy {
        ViewModelProvider(this)[ManageLanguagesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val colorEven : Int = resources.getColor(R.color.even,null)
        val colorOdd : Int = resources.getColor(R.color.odd,null)
        val colorSelected : Int = resources.getColor(R.color.selected,null)

        //Load all languages when creating class
        model.loadAllLanguages()

        //insert new language in the db when typing enter
        binding.newLanguage.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.newLanguage.text.isNotEmpty()) {
                    val lang = Language(binding.newLanguage.text.toString())
                    model.insertLanguage(lang)
                    binding.newLanguage.text.clear()
                }
            }
                false
        }

        //update list after language load query
        model.loadInfo.observe(this) {
            if(it.toMutableList().isNotEmpty()) {
                adapter = LanguagesListRecyclerViewAdapter(it.toMutableList(),colorEven,colorOdd,colorSelected)
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
            else if(it.equals(-1)) {
                notifyError()
                return@observe
            }
            model.loadAllLanguages()
        }

        //insert new language when clicking on button
        binding.add.setOnClickListener {
            if(binding.newLanguage.text.isNotEmpty()) {
                val lang = Language(binding.newLanguage.text.toString())
                model.insertLanguage(lang)
                binding.newLanguage.text.clear()
            }
        }

        binding.delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Êtes-vous sûr.e de vouloir supprimer cette langue ?\n(Cela supprimera tous les dictionnaires et mots associés)")
                .setCancelable(false)
                .setPositiveButton("OUI") { d, _ ->
                    model.deleteLanguage(*adapter.getSelected().toTypedArray())
                    d.dismiss()
                }
                .setNegativeButton("NON") { d, _ ->
                    d.dismiss()
                }
                .show()
        }

    }

    private fun notifyError() {
        Toast.makeText(this,"Une erreur est survenue !",Toast.LENGTH_SHORT).show()
    }
}