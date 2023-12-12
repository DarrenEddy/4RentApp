package com.de.rentalfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.de.rentalfinal.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

val types : List<String> = listOf("All","Condo","House","Apartment")

val tempAddresses : List<String> = listOf(
    "123 abc drive",
    "4 main street",
    "20 avennue road",
    "2334 Bloor Street West"
)

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private var viewList = true

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //lateinits
        this.binding = ActivityMainBinding.inflate(layoutInflater)

        //tool bar
        this.binding.menuToolbar.title = "4Rent"
        setSupportActionBar(this.binding.menuToolbar)

        // map init
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.currentLocation = LatLng(43.74853, -79.26374)

        //spinner Adapter
        val spinnerAdapter = ArrayAdapter<String>(this,R.layout.spinner_row,types)
        binding.spinnerFilterProperties.adapter = spinnerAdapter

        //rv adapter
        val adapter:PropertyAdapter = PropertyAdapter(tempAddresses) { pos -> rowClicked(pos) }
        this.binding.rvProperties.adapter=adapter
        this.binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )


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

    private fun rowClicked(pos: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_item_add_property-> {
                val toast = Toast.makeText(this,"CREATE A NEW ACTIVITY",Toast.LENGTH_SHORT)
                toast.show()
                return true
            }
            R.id.menu_item_login ->
                {
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    return true
                }

            else -> super.onOptionsItemSelected(item)
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
}