package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mrx.arigo.data.remote.response.feature.advertising.AdvertisingResponse
import uz.mrx.arigo.databinding.ItemImageContainerBinding

class AdvertisingAdapter :
    ListAdapter<AdvertisingResponse, AdvertisingAdapter.ViewHolder>(advertisingDiffUtil) {

    inner class ViewHolder(private val binding: ItemImageContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: AdvertisingResponse) {
            Glide.with(binding.root).load(item.image).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

val advertisingDiffUtil = object : DiffUtil.ItemCallback<AdvertisingResponse>() {
    override fun areItemsTheSame(oldItem: AdvertisingResponse, newItem: AdvertisingResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AdvertisingResponse, newItem: AdvertisingResponse): Boolean {
        return oldItem == newItem
    }
}
