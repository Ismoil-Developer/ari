package uz.mrx.arigo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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