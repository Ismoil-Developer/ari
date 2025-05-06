package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.data.remote.model.HistoryModel
import uz.mrx.arigo.databinding.ItemHistoryBinding

class HistoryAdapter(private var onItemClickListener: (HistoryModel) -> Unit) :
    ListAdapter<HistoryModel, HistoryAdapter.ViewHolder>(HistoryModelDiffUtilCallback) {

    private var selectedItemId: Int? = null

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            val newsData = getItem(absoluteAdapterPosition)

            binding.marketName.text = newsData.marketName
            binding.dateOrder.text = newsData.marketDate
            binding.orderPrice.text = newsData.marketCost

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


    object HistoryModelDiffUtilCallback : DiffUtil.ItemCallback<HistoryModel>() {
        override fun areItemsTheSame(oldItem: HistoryModel, newItem: HistoryModel): Boolean {
            return oldItem.marketName == newItem.marketName
        }

        override fun areContentsTheSame(oldItem: HistoryModel, newItem: HistoryModel): Boolean {
            return oldItem == newItem
        }
    }
}
