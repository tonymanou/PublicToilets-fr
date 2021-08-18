package com.tonymanou.pubpoo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tonymanou.pubpoo.R
import com.tonymanou.pubpoo.databinding.ToiletItemBinding
import com.tonymanou.pubpoo.model.Toilet

class ToiletsAdapter : PagingDataAdapter<Toilet, ToiletsAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Toilet>() {
            override fun areItemsTheSame(oldItem: Toilet, newItem: Toilet): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Toilet, newItem: Toilet): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ToiletItemBinding.inflate(inflater, parent, false))
    }

    class ViewHolder(
        private val binding: ToiletItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Toilet?) {
            val binding = binding
            binding.toiletPosition.text = item?.location.toString()
            binding.toiletAccessibility.also {
                val context = it.context
                binding.toiletAddress.text = item?.address
                if (item?.accessible == true) {
                    it.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_accessible))
                    it.contentDescription = context.getString(R.string.toilet_accessibility_yes_description)
                    it.alpha = 1.0f
                } else {
                    it.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_not_accessible))
                    it.contentDescription = context.getString(R.string.toilet_accessibility_no_description)
                    it.alpha = 0.5f
                }
            }
        }
    }

}
