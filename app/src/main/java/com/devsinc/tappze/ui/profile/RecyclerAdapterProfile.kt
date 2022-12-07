package com.devsinc.tappze.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devsinc.tappze.R
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.model.AppIcon

class RecyclerAdapterProfile(private val userLinks: MutableMap<String, String>?) :
    RecyclerView.Adapter<RecyclerAdapterProfile.ProfileViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ProfileViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        if (userLinks != null) {
            holder.textView.text = userLinks.keys.elementAt(position)
            holder.textView.textSize = 12f
            holder.imageView.setImageResource(Constants.getImage(userLinks.keys.elementAt(position)))
            holder.imageView.visibility = View.VISIBLE
            holder.textView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = userLinks?.size ?: 0

    class ProfileViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_app_icon)
        val textView: TextView = itemView.findViewById(R.id.tv_app_name)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }
}