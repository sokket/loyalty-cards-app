package ru.oceancraft.fastcard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.oceancraft.fastcard.databinding.BannerBinding
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.databinding.CardItemBinding

class CardAdapter(
    private val layoutInflater: LayoutInflater,
    private val addButtonListener : () -> Unit,
    private val cardListener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val BANNER_VIEW_TYPE = 0
        const val CARD_VIEW_TYPE = 1
    }

    private val items = ArrayList<Card>()

    fun update(newItems: List<Card>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class BannerViewHolder(val binding: BannerBinding) : RecyclerView.ViewHolder(binding.root)
    inner class CardViewHolder(val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == BANNER_VIEW_TYPE) {
            val binding = BannerBinding.inflate(layoutInflater, parent, false)
            BannerViewHolder(binding)
        } else {
            val binding = CardItemBinding.inflate(layoutInflater, parent, false)
            CardViewHolder(binding)
        }

    }

    override fun getItemViewType(position: Int) =
        if (position == 0) BANNER_VIEW_TYPE else CARD_VIEW_TYPE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> with(holder.binding) {
                addButton.setOnClickListener {
                    addButtonListener.invoke()
                }
            }
            is CardViewHolder -> with(holder.binding) {
                val cardIndex = position - 1
                val card = items[cardIndex]
                cardName.text = card.name
                val color = card.color
                /*cardName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (colorWillBeMasked(color)) R.color.black else R.color.white
                    )
                )*/
                cardColor.setBackgroundColor(color)
                //root.setCardBackgroundColor(color)
                root.setOnClickListener {
                    cardListener.invoke(card.id)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size + 1
}