package com.de.project
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.de.rentalfinal.R
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.models.Shortlist


class ShortListPropertyAdapter(var items:List<Property>,
                               private val deleteBtnClickHandler: (Int) -> Unit
) : RecyclerView.Adapter<ShortListPropertyAdapter.PropertyViewHolder>(){
    inner class PropertyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        init {
            val btnDelete = itemView.findViewById<Button>(R.id.deleteBtn)
            btnDelete.setOnClickListener {
                deleteBtnClickHandler(adapterPosition)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_short_list_item,parent,false)
        return PropertyViewHolder(view)
    }
    fun setData(newList: List<Property>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = items.get(position)

        val tvAddress = holder.itemView.findViewById<TextView>(R.id.tvAddress)
        val tvType = holder.itemView.findViewById<TextView>(R.id.tvType)
        val tvDesc = holder.itemView.findViewById<TextView>(R.id.tvDesc)
        val tvOwner = holder.itemView.findViewById<TextView>(R.id.tvOwner)

        val tvAvailable = holder.itemView.findViewById<TextView>(R.id.tvAvailable)

        tvAddress.setText(property.address)
        tvType.setText(property.type)
        tvDesc.setText(property.description)
        tvOwner.setText(property.owner)
        if (property.available)
        {
            tvAvailable.setText("Available")
        }
        else
        {
            tvAvailable.setText("Unavailable")
        }


    }
}