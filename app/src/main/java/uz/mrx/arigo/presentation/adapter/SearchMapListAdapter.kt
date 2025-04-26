package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.databinding.ItemSearchLocationBinding

class SearchMapListAdapter(private var onItemClickListener: (MapListResponse) -> Unit) :
    ListAdapter<MapListResponse, SearchMapListAdapter.ViewHolder>(MapListDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemSearchLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

            val newsData = getItem(absoluteAdapterPosition)

            binding.marketName.text = newsData.title
            binding.yourLocation.text = newsData.locations



            itemView.setOnClickListener {
                onItemClickListener.invoke(newsData)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    object MapListDiffUtilCallback : DiffUtil.ItemCallback<MapListResponse>() {
        override fun areItemsTheSame(oldItem: MapListResponse, newItem: MapListResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MapListResponse, newItem: MapListResponse): Boolean {
            return oldItem == newItem
        }
    }

}