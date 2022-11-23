package space.timur.tradestatandroid.ui.deals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.databinding.ItemDealBinding

class DealsAdapter: ListAdapter<Deal, DealsAdapter.DealsViewHolder>(DiffCallback()) {

    inner class DealsViewHolder(private val binding: ItemDealBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(deal: Deal){
            binding.apply {
                tickerTitle.text = deal.tickerName
                tickerDate.text = deal.createdDateFormatted
                amountOfTrade.text = deal.amount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsViewHolder {
        val binding = ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Deal>() {
        override fun areItemsTheSame(oldItem: Deal, newItem: Deal) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Deal, newItem: Deal) =
            oldItem == newItem
    }
}