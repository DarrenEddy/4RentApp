package com.de.rentalfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.de.rentalfinal.models.Property


class PropertyAdapter(var items:ArrayList<Property>,private val rowClickHandler: (Int) -> Unit) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>(){
    inner class PropertyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        init {
            itemView.setOnClickListener{
                rowClickHandler(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_property,parent,false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = items.get(position)

        val tvAddress = holder.itemView.findViewById<TextView>(R.id.tvAddress)
        val imageFile = holder.itemView.findViewById<ImageView>(R.id.propertyImage)
        val tvType = holder.itemView.findViewById<TextView>(R.id.tvType)
        val tvAvailable = holder.itemView.findViewById<TextView>(R.id.tvAvailability)
        tvAddress.setText(property.address)
        tvType.setText(property.type)
        if (property.available) {
            tvAvailable.text = "Available"
            tvAvailable.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.availableColor))
        } else {
            tvAvailable.text = "Unavailable"
            tvAvailable.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.unavailableColor))
        }

        val context = holder.itemView.context
        if (property.type == "House") {
            val imagename = "house"
            val res = context.resources.getIdentifier(imagename, "drawable", context.packageName)
            imageFile.setImageResource(res)
        } else if (property.type == "Condo") {
            val imagename = "condo"
            val res = context.resources.getIdentifier(imagename, "drawable", context.packageName)
            imageFile.setImageResource(res)

        } else if (property.type == "Apartment") {
            val imagename = "apartment"
            val res = context.resources.getIdentifier(imagename, "drawable", context.packageName)
            imageFile.setImageResource(res)

        } else if (property.type == "Basement") {
            val imagename = "basement"
            val res = context.resources.getIdentifier(imagename, "drawable", context.packageName)
            imageFile.setImageResource(res)
        }



    }
}