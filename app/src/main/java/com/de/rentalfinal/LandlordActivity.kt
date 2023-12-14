package com.de.rentalfinal

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.de.rentalfinal.databinding.ActivityLandlordBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.de.rentalfinal.repositories.UserRepository

class LandlordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandlordBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var propertyArrayList: ArrayList<Property>
    private  lateinit var  propertyAdapter:PropertyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLandlordBinding.inflate(layoutInflater)
        this.prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        this.propertyRepository = PropertyRepository(applicationContext)
        this.propertyArrayList = ArrayList()
        this.propertyAdapter = PropertyAdapter(propertyArrayList) { pos -> rowClicked(pos) }

        setContentView(this.binding.root)

        this.binding.rvProperties.adapter=propertyAdapter
        this.binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )







    }


    private fun rowClicked(pos: Int) {
        val intent = Intent(this,EditPropertyActivity::class.java)
        intent.putExtra("PROPERTY_ID",propertyArrayList[pos].id)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()

        if (!prefs.contains("USER_EMAIL"))
        {
            goToMain()
            return
        }

        val email = prefs.getString("USER_EMAIL","").toString()

        propertyRepository.getPropertiesByLandlord(email)

        propertyRepository.allProperties.observe(this, androidx.lifecycle.Observer { propertyList ->
            if (propertyList != null) {
                propertyArrayList.clear()
                propertyArrayList.addAll(propertyList)
                propertyAdapter.notifyDataSetChanged()
            }
        } )


    }

    private fun goToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
//
    override fun onBackPressed() {
    super.onBackPressed()
    val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


}