package uz.mrx.arigo.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.data.model.IntroData
import uz.mrx.arigo.databinding.ItemImageContainerBinding

class CarouselAdapter : ListAdapter<IntroData, CarouselAdapter.ViewHolder>(customDiffUtils) {

    inner class ViewHolder(private val binding: ItemImageContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun onBind(introData: IntroData) {

            binding.imageView.setImageResource(introData.image)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselAdapter.ViewHolder {
        return ViewHolder(
            ItemImageContainerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CarouselAdapter.ViewHolder, position: Int) {
        val introData = getItem(position)
        holder.onBind(introData)
    }

}

val customDiffUtils = object : DiffUtil.ItemCallback<IntroData>() {

    override fun areItemsTheSame(oldItem: IntroData, newItem: IntroData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: IntroData, newItem: IntroData): Boolean {

        return oldItem.image == newItem.image

    }

}
