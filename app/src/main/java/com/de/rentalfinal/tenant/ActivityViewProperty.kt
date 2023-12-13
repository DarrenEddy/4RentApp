package com.de.rentalfinal.tenant

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.de.rentalfinal.LoginActivity
import com.de.rentalfinal.MainActivity
import com.de.rentalfinal.R
import com.de.rentalfinal.databinding.ActivityViewPropertyBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.de.rentalfinal.repositories.ShortListRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ActivityViewProperty : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityViewPropertyBinding
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var shortListRepository: ShortListRepository
    private lateinit var prefs: SharedPreferences
    private lateinit var currentUserId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityViewPropertyBinding.inflate(layoutInflater)
        this.propertyRepository = PropertyRepository(applicationContext)
        this.shortListRepository = ShortListRepository(applicationContext)

        this.binding.btnAddToShortList.setOnClickListener(this)
        this.binding.btnGoBack.setOnClickListener(this)
        prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)

        this.currentUserId = this.prefs.getString("USER_EMAIL", "").toString()

        setContentView(this.binding.root)

        val propertyId = intent.getStringExtra("PROPERTY_ID")
        Log.d("PROPERTY_ID", "$propertyId")
        Log.d("USER_ID", "USER_ID: $currentUserId")
        if (propertyId != null){
            propertyRepository.getPropertyById(propertyId,
                onSuccess = {property ->
                    if (property != null){
                        setDetails(property)
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            )
        }

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddToShortList ->{

                if (currentUserId.isNotEmpty()){
                    val propertyId = intent.getStringExtra("PROPERTY_ID")
                    Log.d("PropertyID", "$propertyId")

                    if (propertyId != null) {
                        shortListRepository.getListOfProperties(currentUserId) { properties ->
                            if (properties?.contains(propertyId) == true) {
                                Snackbar.make(binding.root, "Property already added", Snackbar.LENGTH_LONG).show()
                            } else {
                                addToShortList(propertyId)
                                Snackbar.make(binding.root, "Property added to shortlist", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.btnGoBack ->
            {
                finish()
            }
        }
    }
    private fun setDetails(property:Property){
        this.binding.propertyAddress.setText("${property.address}")
        this.binding.propertyType.setText("Type: ${property.type}")
        this.binding.propertyDesc.setText("Description: ${property.description}")
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
    private fun addToShortList(propertyId:String) {
        if(currentUserId.isNotEmpty()){
            shortListRepository.addToShortList(currentUserId, propertyId)
        }
    }
}