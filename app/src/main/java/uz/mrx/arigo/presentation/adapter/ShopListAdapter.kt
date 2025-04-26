package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.databinding.ItemPlaceCardBinding

class ShopListAdapter(private var onItemClickListener: (ShopListResponse) -> Unit) :
    ListAdapter<ShopListResponse, ShopListAdapter.ViewHolder>(ShopListResponseDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemPlaceCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

            val newsData = getItem(absoluteAdapterPosition)
            Glide.with(binding.root.context).load(newsData.image).into(binding.imageViewBackground)

            binding.shopName.text = newsData.title

            binding.textTime.text = newsData.work_start + " " + newsData.work_end

            binding.textAddress.text = newsData.locations


            itemView.setOnClickListener {
                onItemClickListener.invoke(newsData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlaceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    object ShopListResponseDiffUtilCallback : DiffUtil.ItemCallback<ShopListResponse>() {
        override fun areItemsTheSame(oldItem: ShopListResponse, newItem: ShopListResponse): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShopListResponse, newItem: ShopListResponse): Boolean {
            return oldItem == newItem
        }
    }

}