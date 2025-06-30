package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse
import uz.mrx.arigo.databinding.ItemHistoryBinding

class HistoryAdapter(private var onItemClickListener: (OrderHistoryResponse) -> Unit) :
    ListAdapter<OrderHistoryResponse, HistoryAdapter.ViewHolder>(OrderHistoryResponseDiffUtilCallback) {

    private var selectedItemId: Int? = null

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            val newsData = getItem(absoluteAdapterPosition)

            binding.marketName.text = newsData.shop_title
            binding.dateOrder.text = newsData.created_at
            binding.orderPrice.text = newsData.item_price

            itemView.setOnClickListener {
                onItemClickListener.invoke(newsData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()


    object OrderHistoryResponseDiffUtilCallback : DiffUtil.ItemCallback<OrderHistoryResponse>() {
        override fun areItemsTheSame(oldItem: OrderHistoryResponse, newItem: OrderHistoryResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderHistoryResponse, newItem: OrderHistoryResponse): Boolean {
            return oldItem == newItem
        }
    }
}
