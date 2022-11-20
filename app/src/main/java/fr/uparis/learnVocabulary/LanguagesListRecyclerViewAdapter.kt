package fr.uparis.learnVocabulary

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.LanguageDisplayLayoutBinding

class LanguagesListRecyclerViewAdapter(var langs : MutableList<Language>) : RecyclerView.Adapter<LanguagesListRecyclerViewAdapter.VH>() {

    var checked : MutableList<Language> = mutableListOf()

    class VH(val binding : LanguageDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LanguageDisplayLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        var view = VH(binding)

        binding.root.setOnClickListener {
            checked.add(langs[view.absoluteAdapterPosition])
            Log.d(null,"$checked")
        }

        return view
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.langName.text = langs[position].lang

        if(position % 2 == 0)
            (holder.itemView as CardView).setCardBackgroundColor(Color.RED)

        Log.d(null,"$checked, ${langs[position]}")

        if(checked.contains(langs[position]))
            (holder.itemView as CardView).setCardBackgroundColor(Color.GREEN)

    }

    override fun getItemCount() : Int = langs.size

}