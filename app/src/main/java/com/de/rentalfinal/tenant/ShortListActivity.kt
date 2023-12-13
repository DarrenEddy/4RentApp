package com.de.rentalfinal.tenant

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.de.project.ShortListPropertyAdapter
import com.de.rentalfinal.databinding.ActivityShortListBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.de.rentalfinal.repositories.ShortListRepository

class ShortListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShortListBinding
    lateinit var prefs: SharedPreferences
    private lateinit var adapter: ShortListPropertyAdapter
    private lateinit var currentUserId : String
    private var listOfIds : MutableList<String> = mutableListOf()
    private var properties : MutableList<Property> = mutableListOf()
    private lateinit var shortListRepository:ShortListRepository
    private lateinit var propertyRepository: PropertyRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.prefs= getSharedPreferences("com.de.rentalfinal", MODE_PRIVATE)
        this.shortListRepository = ShortListRepository(applicationContext)
        this.propertyRepository = PropertyRepository(applicationContext)

        this.currentUserId = this.prefs.getString("USER_EMAIL", "").toString()
        Log.d("Current user", "Current user: $currentUserId")

        adapter = ShortListPropertyAdapter(emptyList()){pos -> deleteBtnClickHandler(pos)}
        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.goBack.setOnClickListener{
            finish()
        }


    }

    override fun onResume() {
        super.onResume()
        getProperties()

    }
    private fun getProperties(){
        properties.clear()
        listOfIds.clear()
        shortListRepository.getListOfProperties(currentUserId) { propertyIds ->
            if (propertyIds != null) {
                listOfIds.addAll(propertyIds)
                Log.d("Properties initial", "Properties initial: $propertyIds")
                for (id in listOfIds) {
                    propertyRepository.getPropertyById(id) { property ->
                        Log.d("Adding property", "Adding property $property")
                        properties.add(property)
                        adapter.setData(properties)
                    }
                }

            }
        }
    }



    private fun deleteBtnClickHandler(pos:Int){
        val selectedId = properties.get(pos).id

        shortListRepository.deleteFromShortList(currentUserId, selectedId)
        getProperties()

    }
}