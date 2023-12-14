package com.de.project
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.de.rentalfinal.R
import com.de.rentalfinal.models.Property


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
        val imageFile = holder.itemView.findViewById<ImageView>(R.id.propertyImageView)

        val tvAvailable = holder.itemView.findViewById<TextView>(R.id.tvAvailable)

        tvAddress.setText(property.address)
        tvType.setText(property.type)
        tvDesc.setText(property.description)
        tvOwner.setText(property.owner)
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