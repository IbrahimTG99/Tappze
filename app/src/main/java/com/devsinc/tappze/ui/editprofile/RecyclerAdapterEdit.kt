package com.devsinc.tappze.ui.editprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devsinc.tappze.R
import com.devsinc.tappze.model.AppIcon

class RecyclerAdapterEdit(private val appIcons: List<AppIcon>) :
    RecyclerView.Adapter<RecyclerAdapterEdit.EditViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return EditViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        val currentItem = appIcons[position]
        holder.imageView.setImageResource(currentItem.icon)
        holder.textView.text = currentItem.name
        holder.textView.textSize = 12f
    }

    override fun getItemCount() = appIcons.size

    class EditViewHolder(itemView: View, listener: OnItemClickListener) :
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