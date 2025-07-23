package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.response.feature.detail.Feedback
import uz.mrx.arigo.databinding.ItemFeedbackBinding

class FeedBackAdapter (private var onItemClickListener: (Feedback) -> Unit) :
    ListAdapter<Feedback, FeedBackAdapter.ViewHolder>(FeedbackDiffUtilCallback) {

    inner class ViewHolder(private val binding: ItemFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

            val newsData = getItem(absoluteAdapterPosition)

            binding.tvUser.text = newsData.user

            binding.tvDate.text = newsData.created_at

            binding.tvComment.text = newsData.comment

            val rating = newsData.rating  // Masalan, 1-5 oralig'ida
            val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

            stars.forEachIndexed { index, imageView ->
                if (index < rating) {
                    imageView.setImageResource(R.drawable.ic_star_detail) // Yonuvchi yulduz
                } else {
                    imageView.setImageResource(R.drawable.ic_star_empty)  // Bo'sh yulduz
                }
            }

            itemView.setOnClickListener {
                onItemClickListener.invoke(newsData)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFeedbackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

    object FeedbackDiffUtilCallback : DiffUtil.ItemCallback<Feedback>() {

        override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem == newItem
        }

    }

}