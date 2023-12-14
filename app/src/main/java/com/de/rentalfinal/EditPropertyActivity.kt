package com.de.rentalfinal

import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.de.rentalfinal.databinding.ActivityEditPropertyBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import java.util.Locale

class EditPropertyActivity : AppCompatActivity(), OnClickListener,OnEditorActionListener {
    private lateinit var binding: ActivityEditPropertyBinding
    private val TAG = this.javaClass.canonicalName
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var propertyArrayList: ArrayList<Property>
    private lateinit var propertyTypes: List<String>
    private lateinit var prefs: SharedPreferences
    private lateinit var id:String
    private lateinit var property: Property
    private lateinit var email: String
    private var checked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.propertyRepository = PropertyRepository(applicationContext)
        this.propertyArrayList = ArrayList()
        this.binding = ActivityEditPropertyBinding.inflate(layoutInflater)
        this.propertyTypes = types.subList(1,types.size)
        this.prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)



        val adapter = ArrayAdapter<String>(this,R.layout.spinner_row,this.propertyTypes)
        this.binding.spinnerType.adapter = adapter

        if(!prefs.contains("USER_EMAIL"))
        {
            goToMain()
        }
        this.email = prefs.getString("USER_EMAIL","").toString()

        this.id = intent.getStringExtra("PROPERTY_ID").toString()

        if (id == null)
        {
            Toast.makeText(this, "couldnt get id", Toast.LENGTH_SHORT).show()
            goToMain()
            return
        }

        propertyRepository.getPropertyById(id, onSuccess =  { property ->
            if (property != null){
                this.property = property
                this.binding.editAddress.setText(property.address)
                this.binding.editLat.setText(property.lat.toString())
                this.binding.editLng.setText(property.lng.toString())
                this.binding.editDescription.setText(property.description)
                if(!property.available)
                {
                    this.binding.switchAvailable.isChecked = false
                }
                this.binding.spinnerType.setSelection(this.propertyTypes.indexOf(property.type))
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        })





        this.binding.editAddress.setOnEditorActionListener(this)
        this.binding.editLat.setOnEditorActionListener(this)
        this.binding.editLng.setOnEditorActionListener(this)
        this.binding.switchAvailable.setOnClickListener(this)
        this.binding.btnRemoveProperty.setOnClickListener(this)
        this.binding.btnUpdateProperty.setOnClickListener(this)
        setContentView(this.binding.root)
    }
    private fun goToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_update_property ->
                {
                if(validateData())
                {
                    val address = this.binding.editAddress.text.toString()
                    val lat = this.binding.editLat.text.toString().toDouble()
                    val lng = this.binding.editLng.text.toString().toDouble()
                    val desc = this.binding.editDescription.text.toString()
                    val type = this.propertyTypes[this.binding.spinnerType.selectedItemPosition]
                    //this.checked
                    //this.id
                    // this.email
                    val updateProperty = Property(id = this.id,address,lat,lng,type,desc,this.email,this.checked)
                    propertyRepository.updateProperty(updateProperty)

                    val intent = Intent(this,LandlordActivity::class.java)
                    startActivity(intent)


                }
                }
            R.id.btn_remove_property ->
                {
                    propertyRepository.removePropertyById(this.property)
                    val intent = Intent(this,LandlordActivity::class.java)
                    startActivity(intent)
                }
            R.id.switch_available ->
                {
                    this.checked = !this.checked
                }
        }
    }

    private fun validateData():Boolean{
        val address = this.binding.editAddress.text
        val lat = this.binding.editLat.text
        val lng = this.binding.editLng.text
        val desc = this.binding.editDescription.text

        val type = this.propertyTypes[this.binding.spinnerType.selectedItemPosition]
        //this.checked
        //this.id
        // this.email
        if (address.isNullOrEmpty() || lat.isNullOrEmpty() || lng.isNullOrEmpty() || desc.isNullOrEmpty()|| this.email== "" || this.id == "")
        {
            if (address.isNullOrEmpty()){ this.binding.editAddress.setError("Cannot Be Empty")}
            if (lat.isNullOrEmpty()){this.binding.editLat.setError("Cannot Be Empty")}
            if (lng.isNullOrEmpty()){this.binding.editLng.setError("Cannot Be Empty")}
            if (desc.isNullOrEmpty()){this.binding.editDescription.setError("Cannot Be Empty")}
            return false
        }

        return true
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        when (p0?.id) {
            R.id.edit_address -> {

                if (this.binding.editAddress.text.isNullOrEmpty()) {
                    return true
                }
                val address = this.binding.editAddress.text.toString()
                val geocoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())

                try {
                    val searchResults: MutableList<Address>? =
                        geocoder.getFromLocationName(address, 1)
                    if (searchResults == null) {
                        Toast.makeText(this, "Error Searching", Toast.LENGTH_SHORT).show()
                        return true
                    }
                    if (searchResults.size == 0) {
                        binding.editAddress.setError("Cant Find Address")

                    } else {
                        val foundLocation: Address = searchResults[0]
                        this.binding.editLng.setText(foundLocation.longitude.toString())
                        this.binding.editLat.setText(foundLocation.latitude.toString())


                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Error encountered while getting coordinate location.")
                    Log.e(TAG, ex.toString())
                }


                return true
            }
            R.id.edit_lat->
                {
                    if (this.binding.editLat.text.isNullOrEmpty()){
                        return true
                    }
                    val lat = this.binding.editLat.text.toString().toDouble()
                    val lng = this.binding.editLng.text.toString().toDouble()
                    val geocoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())

                    try {
                        val searchResults: MutableList<Address>? =
                            geocoder.getFromLocation(lat,lng,1)
                        if (searchResults == null) {
                            Toast.makeText(this, "Error Searching", Toast.LENGTH_SHORT).show()
                            return true
                        }
                        if (searchResults.size == 0) {
                            binding.editLat.setError("Cant Find Address")

                        } else {
                            val foundLocation: Address = searchResults[0]
                            this.binding.editAddress.setText(foundLocation.getAddressLine(0))

                        }
                    } catch (ex: Exception) {
                        Log.e(TAG, "Error encountered while getting coordinate location.")
                        Log.e(TAG, ex.toString())
                    }


                    return true


                }
            R.id.edit_lng->
            {
                if (this.binding.editLng.text.isNullOrEmpty()){
                    return true
                }
                val lat = this.binding.editLat.text.toString().toDouble()
                val lng = this.binding.editLng.text.toString().toDouble()
                val geocoder: Geocoder = Geocoder(applicationContext, Locale.getDefault())

                try {
                    val searchResults: MutableList<Address>? =
                        geocoder.getFromLocation(lat,lng,1)
                    if (searchResults == null) {
                        Toast.makeText(this, "Error Searching", Toast.LENGTH_SHORT).show()
                        return true
                    }
                    if (searchResults.size == 0) {
                        binding.editLat.setError("Cant Find Address")

                    } else {
                        val foundLocation: Address = searchResults[0]
                        this.binding.editAddress.setText(foundLocation.getAddressLine(0))

                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Error encountered while getting coordinate location.")
                    Log.e(TAG, ex.toString())
                }


                return true


            }

            else ->
                {
                    return true
                }

        }

    }
}