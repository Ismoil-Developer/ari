package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mrx.arigo.data.model.MagazineData
import uz.mrx.arigo.data.remote.response.feature.ShopData
import uz.mrx.arigo.databinding.ItemMagazineBinding

class PharmacyAdapter(private var onItemClickListener: (ShopData) -> Unit) :
    ListAdapter<ShopData, PharmacyAdapter.ViewHolder>(ShopDataDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemMagazineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

            val newsData = getItem(absoluteAdapterPosition)
            Glide.with(binding.root.context).load(newsData.image).into(binding.imageView)

            binding.title.text = newsData.title


            itemView.setOnClickListener {
                onItemClickListener.invoke(newsData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMagazineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    object ShopDataDiffUtilCallback : DiffUtil.ItemCallback<ShopData>() {
        override fun areItemsTheSame(oldItem: ShopData, newItem: ShopData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShopData, newItem: ShopData): Boolean {
            return oldItem == newItem
        }
    }

}