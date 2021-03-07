package ru.oceancraft.fastcard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.oceancraft.fastcard.model.Card
import ru.oceancraft.fastcard.databinding.CardItemBinding
import ru.oceancraft.fastcard.databinding.FooterBinding

class CardAdapter(
    private val layoutInflater: LayoutInflater,
    private val backupButtonListener: () -> Unit,
    private val cardListener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val CARD_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = 1
    }

    private val items = ArrayList<Card>()

    fun update(newItems: List<Card>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class FooterViewHolder(val binding: FooterBinding) : RecyclerView.ViewHolder(binding.root)
    inner class CardViewHolder(val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val binding = FooterBinding.inflate(layoutInflater, parent, false)
            FooterViewHolder(binding)
        } else {
            val binding = CardItemBinding.inflate(layoutInflater, parent, false)
            CardViewHolder(binding)
        }

    }

    override fun getItemViewType(position: Int) =
        if (position == itemCount - 1) FOOTER_VIEW_TYPE else CARD_VIEW_TYPE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FooterViewHolder -> with(holder.binding) {
                backupButton.setOnClickListener {
                    backupButtonListener.invoke()
                }
            }
            is CardViewHolder -> with(holder.binding) {
                val card = items[position]
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