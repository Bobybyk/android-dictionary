package fr.uparis.learnVocabulary.recyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.learnVocabulary.database.entities.Word
import fr.uparis.learnVocabulary.databinding.WordDisplayLayoutBinding

class WordsListRecyclerViewAdapter(private var langs : MutableList<Word>, private var colorEven : Int, private var colorOdd: Int, private var colorSelected : Int) : RecyclerView.Adapter<WordsListRecyclerViewAdapter.VH>() {

    //The list containing the selected elements
    private var checked : MutableList<Word> = mutableListOf()

    class VH(val binding : WordDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = WordDisplayLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

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
        holder.binding.word.text = langs[position].word
        holder.binding.srcLang.text = langs[position].sourceLanguage
        holder.binding.dstLang.text = langs[position].destinationLanguage
        holder.binding.timesRemembered.text = langs[position].timesRemembered.toString()

        if(langs[position].timesRemembered > 4) {
            holder.binding.ruler.visibility = VISIBLE
            holder.binding.knownMessage.visibility = VISIBLE
        }

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