package fr.uparis.learnVocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.LanguageDisplayLayoutBinding

class LanguagesListRecyclerViewAdapter(var langs : MutableList<Language>) : RecyclerView.Adapter<LanguagesListRecyclerViewAdapter.VH>() {

    class VH(val binding : LanguageDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LanguageDisplayLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.langName.text = langs[position].lang
    }

    override fun getItemCount() : Int = langs.size

}