package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mrx.arigo.data.remote.response.feature.ShopData
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.databinding.ItemLocationBinding
import uz.mrx.arigo.databinding.ItemMagazineBinding

class LocationAdapter(private var onItemClickListener: (LocationCreateResponse) -> Unit, private val edtClickListener: (LocationCreateResponse) -> Unit) :
    ListAdapter<LocationCreateResponse, LocationAdapter.ViewHolder>(LocationCreateResponseDiffUtilCallback) {

    private var selectedItemId: Int? = null

    inner class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            val newsData = getItem(absoluteAdapterPosition)

            binding.locationName.text = newsData.custom_name
            binding.yourLocation.text = newsData.address

            if (newsData.id == selectedItemId) {
                binding.icSelected.visibility = View.VISIBLE
                binding.icUnselected.visibility = View.GONE
            } else {
                binding.icSelected.visibility = View.GONE
                binding.icUnselected.visibility = View.VISIBLE
            }

            binding.icEdt.setOnClickListener {
                edtClickListener.invoke(newsData)
            }

            binding.save.setOnClickListener {
                selectedItemId = newsData.id
                onItemClickListener.invoke(newsData)
                notifyDataSetChanged() // Adapterni yangilash
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    fun setSelectedItem(id: Int) {
        selectedItemId = id
        notifyDataSetChanged()
    }

    object LocationCreateResponseDiffUtilCallback : DiffUtil.ItemCallback<LocationCreateResponse>() {
        override fun areItemsTheSame(oldItem: LocationCreateResponse, newItem: LocationCreateResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationCreateResponse, newItem: LocationCreateResponse): Boolean {
            return oldItem == newItem
        }
    }
}
