package com.de.rentalfinal

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.de.rentalfinal.databinding.ActivityCreatePropertyBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

import java.util.Locale

class CreatePropertyActivity : AppCompatActivity(),OnClickListener {
    private val TAG = this.javaClass.canonicalName
    private lateinit var binding:ActivityCreatePropertyBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var email:String
    private lateinit var propertyRepository: PropertyRepository
    private var available = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCreatePropertyBinding.inflate(layoutInflater)
        this.propertyRepository = PropertyRepository(applicationContext)
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)

        val adapter = ArrayAdapter<String>(this,R.layout.spinner_row,types.subList(1, types.size))
        this.binding.spinnerType.adapter = adapter

        if (prefs.contains("USER_EMAIL")) {
            this.email = this.prefs.getString("USER_EMAIL", "").toString()
        }
        else
        {
            goToMain()
        }

        this.binding.switchAvailable.setOnClickListener(this)
        this.binding.btnCreateProperty.setOnClickListener(this)
        this.binding.btnUseCurrentLocation.setOnClickListener(this)


        setContentView(this.binding.root)
    }





    private fun goToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_use_current_location ->
                {
                    getDeviceLocation()
                }

            R.id.btn_create_property ->
                {
                    if (validateData())
                    {
                        val address = this.binding.editAddress.text.toString()
                        val description = this.binding.editDescription.text.toString()
                        val type = types.subList(1,types.size)[this.binding.spinnerType.selectedItemPosition]
                        // available
                        //email

                        // check if address is a valid address
                        val geocoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())

                        try {
                            val searchResults:MutableList<Address>? = geocoder.getFromLocationName(address, 1)
                            if (searchResults == null) {
                                Toast.makeText(this, "Error Searching", Toast.LENGTH_SHORT).show()
                                return
                            }
                            if (searchResults.size == 0) {
                                binding.editAddress.setError("Search Results Empty")

                            } else {
                                val foundLocation: Address = searchResults[0]
                                val newProperty = Property(address = address, lat = foundLocation.latitude, lng = foundLocation.longitude, type = type, description = description, owner = email, available = available)
                                addProperty(newProperty)
                            }
                        } catch(ex:Exception) {
                            Log.e(TAG, "Error encountered while getting coordinate location.")
                            Log.e(TAG, ex.toString())
                        }
                    }
                }
            R.id.switch_available ->
                {
                    available = !available
                }
        }

    }

    private fun validateData():Boolean
    {
        val address = this.binding.editAddress.text
        val description = this.binding.editDescription.text
        if (address.isNullOrEmpty() || description.isNullOrEmpty())
        {
            if(address.isNullOrEmpty()) this.binding.editAddress.setError("Cannot Be Empty")
            if (description.isNullOrEmpty()) this.binding.editDescription.setError("Cannot Be Empty")
            return false
        }
        return true
    }

    private fun getDeviceLocation() {
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "need to enable permissions", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location === null) {
                    Log.d(TAG, "Location is null")
                    return@addOnSuccessListener
                }
                // Output the location
                val message = "The device is located at: ${location.latitude}, ${location.longitude}"
                Log.d(TAG, message)
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()

                val geocoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())

                try {
                    val searchResults: MutableList<Address>? =
                        geocoder.getFromLocation(location.latitude,location.longitude,1)
                    if (searchResults == null) {
                        Toast.makeText(this, "Error Searching", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    if (searchResults.size == 0) {
                        binding.editAddress.setError("Cant Find Address")

                    } else {
                        val foundLocation: Address = searchResults[0]
                        this.binding.editAddress.setText(foundLocation.getAddressLine(0))

                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Error encountered while getting coordinate location.")
                    Log.e(TAG, ex.toString())
                }

            }
    }

    private fun addProperty(newProperty: Property)
    {
        propertyRepository.addPropertyToDB(newProperty)
        goToMain()
    }
}