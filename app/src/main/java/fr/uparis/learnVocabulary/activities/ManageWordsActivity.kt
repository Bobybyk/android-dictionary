package fr.uparis.learnVocabulary.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.learnVocabulary.database.entities.Word
import fr.uparis.learnVocabulary.databinding.ActivityManageWordsBinding
import fr.uparis.learnVocabulary.recyclerViews.WordsListRecyclerViewAdapter
import fr.uparis.learnVocabulary.viewModels.ManageWordsViewModel

class ManageWordsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityManageWordsBinding
    private lateinit var adapter : WordsListRecyclerViewAdapter
    private val model by lazy {
        ViewModelProvider(this)[ManageWordsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val colorEven : Int = resources.getColor(fr.uparis.learnVocabulary.R.color.even,null)
        val colorOdd : Int = resources.getColor(fr.uparis.learnVocabulary.R.color.odd,null)
        val colorSelected : Int = resources.getColor(fr.uparis.learnVocabulary.R.color.selected,null)

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

        binding.add.setOnClickListener {
            val newWord = Word(binding.newWord.text.toString(), binding.lFrom.selectedItem.toString(), binding.lTo.selectedItem.toString())
            model.insertWord(newWord)
        }

        binding.delete.setOnClickListener {
            model.deleteWord(*adapter.getSelected().toTypedArray())
        }

        model.deleteInfo.observe(this) {
            if(it >= 0) {
                binding.newWord.text.clear()
                model.loadAllWords()
            } else
                Toast.makeText(this,"Une erreur est survenue !", Toast.LENGTH_SHORT).show()
        }

        model.insertInfo.observe(this) {
            if(it >= 0) {
                binding.newWord.text.clear()
                model.loadAllWords()
            } else
                Toast.makeText(this,"Une erreur est survenue !", Toast.LENGTH_SHORT).show()
        }

        model.loadAllWords()
        model.wordLoadInfo.observe(this) {
            Log.d(null, "$it")
            adapter = WordsListRecyclerViewAdapter(it.toMutableList(),colorEven,colorOdd,colorSelected)
            binding.recyclerView.adapter = adapter
        }

    }
}