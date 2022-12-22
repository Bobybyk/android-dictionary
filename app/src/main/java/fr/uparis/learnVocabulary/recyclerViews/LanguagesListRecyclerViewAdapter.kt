package fr.uparis.learnVocabulary.recyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.learnVocabulary.database.entities.Language
import fr.uparis.learnVocabulary.databinding.LanguageDisplayLayoutBinding


class LanguagesListRecyclerViewAdapter(private var langs : MutableList<Language>, private var colorEven : Int, private var colorOdd: Int, private var colorSelected : Int) : RecyclerView.Adapter<LanguagesListRecyclerViewAdapter.VH>() {

    //The list containing the selected elements
    private var checked : MutableList<Language> = mutableListOf()

    class VH(val binding : LanguageDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LanguageDisplayLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val view = VH(binding)

        binding.root.setOnClickListener {
            val item = langs[view.absoluteAdapterPosition]

            if(checked.contains(item)) {
                checked.remove(item)
                setColor(view.itemView,view.absoluteAdapterPosition)
            } else {
                checked.add(item)
                (view.itemView as CardView).setCardBackgroundColor(colorSelected)
            }
        }

        return view
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.langName.text = langs[position].lang

        setColor(holder.itemView,position)
    }

    //returns the number of selected elements
    override fun getItemCount() : Int = langs.size

    //returns  the selected elements list
    fun getSelected() = checked

    //functionused to set the color of elements inside the recycler depending on their position
    private fun setColor(item : View, position: Int) {

        if(position % 2 == 0)
            (item as CardView).setCardBackgroundColor(colorEven)
        else
            (item as CardView).setCardBackgroundColor(colorOdd)
    }

}