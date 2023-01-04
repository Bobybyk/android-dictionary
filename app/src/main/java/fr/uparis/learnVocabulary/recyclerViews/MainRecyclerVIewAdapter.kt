package fr.uparis.learnVocabulary.recyclerViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.learnVocabulary.database.entities.Dictionary
import fr.uparis.learnVocabulary.databinding.DictionnaryDisplayLayoutBinding

class MainRecyclerVIewAdapter(private var dicos : List<Dictionary>, private var colorEven : Int, private var colorOdd: Int) : RecyclerView.Adapter<MainRecyclerVIewAdapter.VH>() {

    class VH(val binding : DictionnaryDisplayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = DictionnaryDisplayLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.srcLang.text = dicos[position].sourceLanguage
        holder.binding.dstLang.text = dicos[position].destinationLanguage
        holder.binding.url.text = dicos[position].url

        val color = if(position % 2 == 0) colorEven else colorOdd

        (holder.itemView as CardView).setCardBackgroundColor(color)

    }

    //returns the number of selected elements
    override fun getItemCount() : Int = dicos.size
}