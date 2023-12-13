package com.de.rentalfinal.tenant

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.de.rentalfinal.MainActivity
import com.de.rentalfinal.R
import com.de.rentalfinal.databinding.ActivityViewPropertyBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.google.android.material.snackbar.Snackbar

class ActivityViewProperty : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityViewPropertyBinding
    private lateinit var propertyRepository: PropertyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityViewPropertyBinding.inflate(layoutInflater)
        this.propertyRepository = PropertyRepository(applicationContext)

        this.binding.btnAddToShortList.setOnClickListener(this)

        setContentView(this.binding.root)

        val propertyId = intent.getStringExtra("PROPERTY_ID")
        Log.d("PROPERTY_ID", "$propertyId")
        if (propertyId != null){
            propertyRepository.getPropertyById(propertyId,
                onSuccess = {property ->
                    if (property != null){
                        setDetails(property)
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                },
                onFailure = {ex -> Log.d("ViewPropertyActivity", "ViewProperty: Could not show property due to exception: $ex") }
            )
        }

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddToShortList ->
            {

            }
        }
    }
    private fun setDetails(property:Property){
        this.binding.propertyAddress.setText("${property.address}")
        this.binding.propertyType.setText("Type: ${property.type}")
        this.binding.propertyDesc.setText("Description: $property.description}")
        if (property.available)
        {
            this.binding.propertyAvailability.setText("Available")
            this.binding.propertyAvailability.setTextColor(Color.rgb(1,100,32))
        }
        else
        {
            this.binding.propertyAvailability.setText("Unavailable")
            this.binding.propertyAvailability.setTextColor(Color.rgb(255,0,0))
        }
        this.binding.propertyContact.setText("Contact: ${property.owner}")

        if (property.type == "House") {
            val imagename = "house"
            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
            this.binding.typeImage.setImageResource(res)
        } else if (property.type == "Condo") {
            val imagename = "condo"
            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
            this.binding.typeImage.setImageResource(res)

        } else if (property.type == "Apartment") {
            val imagename = "apartment"
            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
            this.binding.typeImage.setImageResource(res)

        } else if (property.type == "Basement") {
            val imagename = "basement"
            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
            this.binding.typeImage.setImageResource(res)
        }
    }
}