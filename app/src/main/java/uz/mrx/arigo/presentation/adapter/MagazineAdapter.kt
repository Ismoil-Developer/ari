package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.mrx.arigo.data.remote.response.feature.ShopData
import uz.mrx.arigo.databinding.ItemMagazineBinding

class MagazineAdapter(private var onItemClickListener: (ShopData) -> Unit) :
    ListAdapter<ShopData, MagazineAdapter.ViewHolder>(ShopDataDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemMagazineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind() {
            val shop = getItem(absoluteAdapterPosition)

            // Rasmni yuklash
            Glide.with(binding.root.context).load(shop.image).into(binding.imageView)

            // Nomi
            binding.title.text = shop.title

            // Qulf belgisini ko‘rsatish (faqat is_active = false bo‘lsa)
            if (shop.is_active) {
                binding.lock.visibility = View.GONE
                itemView.setOnClickListener {
                    onItemClickListener.invoke(shop)
                }
            } else {
                binding.lock.visibility = View.VISIBLE
                itemView.setOnClickListener(null) // bosilmaydi
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