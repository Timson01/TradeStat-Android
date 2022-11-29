package space.timur.tradestatandroid.ui.deals

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.timur.tradestatandroid.R
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.databinding.ItemDealBinding

class DealsAdapter(private val listener: OnItemClickListener): ListAdapter<Deal, DealsAdapter.DealsViewHolder>(DiffCallback()) {

    inner class DealsViewHolder(private val binding: ItemDealBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val deal = getItem(position)
                        listener.onItemClick(deal)
                    }
                }
                root.setOnLongClickListener {
                    val popupMenu = PopupMenu(root.context, it)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_edit -> {
                                val position = adapterPosition
                                if(position != RecyclerView.NO_POSITION){
                                    val deal = getItem(position)
                                    listener.onItemLongClick(deal)
                                }
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.inflate(R.menu.menu_edit_deals)
                    try {
                        val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                        fieldMPopup.isAccessible = true
                        val mPopup = fieldMPopup.get(popupMenu)
                        mPopup.javaClass
                            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                            .invoke(mPopup, true)
                    } catch (e: Exception) {
                        Log.e("Main", "Error showing menu icons.", e)
                    } finally {
                        popupMenu.show()
                    }
                    true
                }
            }
        }

        fun bind(deal: Deal){
            binding.apply {
                tickerTitle.text = deal.tickerName
                tickerDate.text = deal.createdDateFormattedWithTime
                if(deal.amount < 0){
                    amountOfTrade.setTextColor(Color.RED)
                }else{
                    amountOfTrade.setTextColor(Color.GREEN)
                }
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

    interface OnItemClickListener {
        fun onItemClick(deal: Deal)
        fun onItemLongClick(deal: Deal)
    }

    class DiffCallback : DiffUtil.ItemCallback<Deal>() {
        override fun areItemsTheSame(oldItem: Deal, newItem: Deal) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Deal, newItem: Deal) =
            oldItem == newItem
    }
}