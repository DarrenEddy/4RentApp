package com.de.rentalfinal.repositories


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.de.rentalfinal.models.Property
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.firestore
import kotlin.math.log

//Controller
class PropertyRepository(private val context : Context) {
    private val TAG = this.toString();

    //get an instance of firestore database
    private val db = Firebase.firestore

    private val COLLECTION_EXPENSES = "Properties"
    private val COLLECTION_USERS = "Users"

    private val FIELD_PROPERTY_ADDRESS = "address"
    private val FIELD_PROPERTY_TYPE = "type"
    private val FIELD_PROPERTY_DESCRIPTION = "description";
    private val FIELD_PROPERTY_OWNER= "owner";
    private val FIELD_PROPERTY_AVAILABLE = "available";

    var allProperties: MutableLiveData<List<Property>> = MutableLiveData<List<Property>>()

    private var loggedInUserEmail = ""
    private lateinit var sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences("com.de.rentalfinal", Context.MODE_PRIVATE)

        if (sharedPrefs.contains("USER_EMAIL")) {
            loggedInUserEmail = sharedPrefs.getString("USER_EMAIL", "NA").toString()
        }
    }

    fun addPropertyToDB(newProperty : Property){
        if (loggedInUserEmail.isNotEmpty()) {
            try {
                val data: MutableMap<String, Any> = HashMap();

                data[FIELD_PROPERTY_ADDRESS] = newProperty.address;
                data[FIELD_PROPERTY_TYPE] = newProperty.type
                data[FIELD_PROPERTY_DESCRIPTION] = newProperty.description
                data[FIELD_PROPERTY_OWNER] = newProperty.owner
                data[FIELD_PROPERTY_AVAILABLE] = newProperty.available

            db.collection(COLLECTION_EXPENSES)
                .add(data)
                .addOnSuccessListener { docRef ->
                    Log.d(TAG, "addPropertyToDB: Document successfully added with ID : ${docRef.id}")
                }
                .addOnFailureListener { ex ->
                    Log.e(TAG, "addPropertyToDB: Exception ocurred while adding a document : $ex", )
                }

            }
            catch (ex : java.lang.Exception){
                Log.d(
                    TAG,
                    "addPropertyToDB: Couldn't perform insert on Expenses collection due to exception $ex"
                )
            }
        }else{
            Log.e(TAG, "addPropertyToDB: Cannot create expense without user's email address. You must create the account first.", )
        }
    }


}
