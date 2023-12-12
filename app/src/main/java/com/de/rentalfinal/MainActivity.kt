package com.de.rentalfinal

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.de.rentalfinal.databinding.ActivityMainBinding
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.repositories.PropertyRepository
import com.de.rentalfinal.repositories.UserRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

val types : List<String> = listOf("All","Condo","House","Apartment")

val tempAddresses : List<String> = listOf(
    "123 abc drive",
    "4 main street",
    "20 avennue road",
    "2334 Bloor Street West"
)

class MainActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var viewList = true
    private var currentUserType = ""

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var locationCallback: LocationCallback
    private lateinit var prefs: SharedPreferences
    private lateinit var userRepository: UserRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var propertyRepository:PropertyRepository
    private lateinit var propertyArrayList: ArrayList<Property>
    private  lateinit var  propertyAdapter:PropertyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //lateinits
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        this.userRepository = UserRepository(applicationContext)
        this.propertyRepository = PropertyRepository(applicationContext)
        this.firebaseAuth = FirebaseAuth.getInstance()




        // map init
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.currentLocation = LatLng(43.74853, -79.26374)

        //spinner Adapter
        val spinnerAdapter = ArrayAdapter<String>(this,R.layout.spinner_row,types)
        binding.spinnerFilterProperties.adapter = spinnerAdapter
        binding.spinnerFilterProperties.onItemSelectedListener = this

        //rv adapter
        propertyArrayList = ArrayList()
        propertyAdapter = PropertyAdapter(propertyArrayList) { pos -> rowClicked(pos) }
        this.binding.rvProperties.adapter=propertyAdapter
        this.binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        //ToolBar
        this.binding.menuToolbar.title = "4Rent"
        setSupportActionBar(this.binding.menuToolbar)


        setContentView(binding.root)

        //onclicks
        binding.switchMapList.setOnClickListener{
            if (viewList)
            {
                // change to map view
                this.binding.rvProperties.visibility = View.GONE
                viewList = false
            }
            else
            {
                //change to list view
                this.binding.rvProperties.visibility = View.VISIBLE
                viewList = true
            }
        }
   

        }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.currentLocation, 20.0f))
        mMap.addMarker(MarkerOptions().position(this.currentLocation).title("You're Here"))

        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.isTrafficEnabled = true

        val uiSettings = p0.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true

    }

    override fun onResume() {
        super.onResume()
        if (prefs.contains("USER_EMAIL"))
        {
            userRepository.getTypeByEmail(prefs.getString("USER_EMAIL","").toString(),this.binding.menuToolbar)
        }

        propertyRepository.retrieveAllProperties()
        propertyRepository.allProperties.observe(this, androidx.lifecycle.Observer { propertyList ->
            if (propertyList != null) {
                propertyArrayList.clear()
                propertyArrayList.addAll(propertyList)
                propertyAdapter.notifyDataSetChanged()
            }
        } )
    }


    private fun updateMenu()
    {
        val type = prefs.getString("USER_TYPE","").toString()
        if (type == "Landlord")
        {
            this.binding.menuToolbar.menu.clear()
            this.binding.menuToolbar.inflateMenu(R.menu.menu_options_landlord)
        }
        else
        {
            this.binding.menuToolbar.menu.clear()
            this.binding.menuToolbar.inflateMenu(R.menu.menu_options)
        }

    }
    private fun rowClicked(pos: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateMenu()
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){

            R.id.menu_item_add_property-> {
                val intent = Intent(this,CreatePropertyActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_item_login ->
                {
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    return true
                }
            R.id.menu_item_logout ->
                {
                    prefs.edit().remove("USER_EMAIL").commit()
                    prefs.edit().remove("USER_TYPE").commit()
                    firebaseAuth.signOut()
                    updateMenu()
                    return true
                }


            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2 == 0)
        {onResume()}
        else
        {
            propertyRepository.getPropertiesByType(type = types[p2])
            propertyRepository.allProperties.observe(this, androidx.lifecycle.Observer { propertyList ->
                if (propertyList != null) {
                    propertyArrayList.clear()
                    propertyArrayList.addAll(propertyList)
                    propertyAdapter.notifyDataSetChanged()
                }
            } )
        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}