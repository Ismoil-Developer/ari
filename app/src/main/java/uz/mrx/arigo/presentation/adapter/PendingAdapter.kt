package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.databinding.ItemOrderBinding

class PendingAdapter(private var onItemClickListener: (OrderPendingSearchResponse) -> Unit) :
    ListAdapter<OrderPendingSearchResponse, PendingAdapter.ViewHolder>(OrderPendingSearchResponseDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind() {
            val shop = getItem(absoluteAdapterPosition)

            // Nomi
            binding.textView.text = shop.order_code
            binding.textView2.text = shop.shop_title

            itemView.setOnClickListener {
                onItemClickListener.invoke(shop)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    object OrderPendingSearchResponseDiffUtilCallback : DiffUtil.ItemCallback<OrderPendingSearchResponse>() {
        override fun areItemsTheSame(oldItem: OrderPendingSearchResponse, newItem: OrderPendingSearchResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderPendingSearchResponse, newItem: OrderPendingSearchResponse): Boolean {
            return oldItem == newItem
        }
    }

}