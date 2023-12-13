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

    private val COLLECTION_PROPERTY = "Properties"
    private val COLLECTION_USERS = "Users"

    private val FIELD_PROPERTY_ID = "id"
    private val FIELD_PROPERTY_ADDRESS = "address"
    private val FIELD_PROPERTY_TYPE = "type"
    private val FIELD_PROPERTY_DESCRIPTION = "description";
    private val FIELD_LAT = "lat"
    private val FIELD_LNG = "lng"
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

    fun retrieveAllProperties(){
            try{
                db.collection(COLLECTION_PROPERTY)
                    .addSnapshotListener(EventListener{ result, error ->
                        if (error != null){
                            Log.e(TAG,
                                "retrieveAllProperties: Listening to Properties collection failed due to error : $error", )
                            return@EventListener
                        }

                        if (result != null){
                            Log.d(TAG, "retrieveAllPropertis: Number of documents retrieved : ${result.size()}")

                            val tempList : MutableList<Property> = ArrayList<Property>()

                            for (docChanges in result.documentChanges){

                                val currentDocument : Property = docChanges.document.toObject(Property::class.java)
                                Log.d(TAG, "retrieveAllProperties: currentDocument : $currentDocument")

                                when(docChanges.type){
                                    DocumentChange.Type.ADDED -> {
                                        //do necessary changes to your local list of objects
                                        tempList.add(currentDocument)
                                    }
                                    DocumentChange.Type.MODIFIED -> {

                                    }
                                    DocumentChange.Type.REMOVED -> {

                                    }
                                }
                            }//for
                            Log.d(TAG, "retrieveAllProperties: tempList : $tempList")
                            //replace the value in allProperties

                            allProperties.postValue(tempList)

                        }else{
                            Log.d(TAG, "retrieveAllProperties: No data in the result after retrieving")
                        }
                    })


            }
            catch (ex : java.lang.Exception){
                Log.e(TAG, "retrieveAllProperties: Unable to retrieve all Properties : $ex", )
            }

    }
    fun getPropertiesByType(type:String)
    {

            try{
                db.collection(COLLECTION_PROPERTY).whereEqualTo(FIELD_PROPERTY_TYPE,type)
                    .addSnapshotListener(EventListener { result, error ->
                        //check for result or errors and update UI accordingly
                        if (error != null){
                            Log.e(TAG,
                                "filterProperties: Listening to Properties collection failed due to error : $error", )
                            return@EventListener
                        }

                        if (result != null){
                            Log.d(TAG, "filterProperties: Number of documents retrieved : ${result.size()}")

                            val tempList : MutableList<Property> = ArrayList<Property>()

                            for (docChanges in result.documentChanges){

                                val currentDocument : Property = docChanges.document.toObject(Property::class.java)
                                Log.d(TAG, "filterProperties: currentDocument : $currentDocument")

                                //do necessary changes to your local list of objects
                                tempList.add(currentDocument)
                            }//for
                            Log.d(TAG, "filterProperties: tempList : $tempList")
                            //replace the value in allProperties
                            allProperties.postValue(tempList)

                        }else{
                            Log.d(TAG, "filterProperties: No data in the result after retrieving")
                        }
                    })
            }
            catch (ex : java.lang.Exception){
                Log.e(TAG, "filterProperties: Unable to filter Properties : $ex", )
            }

    }

    fun getPropertiesByLandlord(email:String)
    {

        try{
            db.collection(COLLECTION_PROPERTY).whereEqualTo(FIELD_PROPERTY_OWNER,email)
                .addSnapshotListener(EventListener { result, error ->
                    //check for result or errors and update UI accordingly
                    if (error != null){
                        Log.e(TAG,
                            "filterProperties: Listening to Properties collection failed due to error : $error", )
                        return@EventListener
                    }

                    if (result != null){
                        Log.d(TAG, "filterProperties: Number of documents retrieved : ${result.size()}")

                        val tempList : MutableList<Property> = ArrayList<Property>()

                        for (docChanges in result.documentChanges){

                            val currentDocument : Property = docChanges.document.toObject(Property::class.java)
                            Log.d(TAG, "filterProperties: currentDocument : $currentDocument")

                            //do necessary changes to your local list of objects
                            tempList.add(currentDocument)
                        }//for
                        Log.d(TAG, "filterProperties: tempList : $tempList")
                        //replace the value in allProperties
                        allProperties.postValue(tempList)

                    }else{
                        Log.d(TAG, "filterProperties: No data in the result after retrieving")
                    }
                })
        }
        catch (ex : java.lang.Exception){
            Log.e(TAG, "filterProperties: Unable to filter Properties : $ex", )
        }

    }

    fun getPropertyById(id:String)
    {
        try{
            db.collection(COLLECTION_PROPERTY).whereEqualTo(FIELD_PROPERTY_ID,id)
                .addSnapshotListener(EventListener { result, error ->
                    //check for result or errors and update UI accordingly
                    if (error != null){
                        Log.e(TAG,
                            "filterProperties: Listening to Properties collection failed due to error : $error", )
                        return@EventListener
                    }

                    if (result != null){
                        Log.d(TAG, "filterProperties: Number of documents retrieved : ${result.size()}")

                        val tempList : MutableList<Property> = ArrayList<Property>()

                        for (docChanges in result.documentChanges){

                            val currentDocument : Property = docChanges.document.toObject(Property::class.java)
                            Log.d(TAG, "filterProperties: currentDocument : $currentDocument")

                            //do necessary changes to your local list of objects
                            tempList.add(currentDocument)
                        }//for
                        Log.d(TAG, "filterProperties: tempList : $tempList")
                        //replace the value in allProperties
                        allProperties.postValue(tempList)

                    }else{
                        Log.d(TAG, "filterProperties: No data in the result after retrieving")
                    }
                })
        }
        catch (ex : java.lang.Exception){
            Log.e(TAG, "filterProperties: Unable to filter Properties : $ex", )
        }

    }

    fun removePropertyById(rmbProperty:Property){
        db.collection(COLLECTION_PROPERTY).document(rmbProperty.id).delete()
    }

    fun updateProperty(newProperty: Property)
    {
        val data: MutableMap<String, Any> = HashMap();

        data[FIELD_PROPERTY_ADDRESS] = newProperty.address;
        data[FIELD_PROPERTY_TYPE] = newProperty.type
        data[FIELD_PROPERTY_DESCRIPTION] = newProperty.description
        data[FIELD_PROPERTY_OWNER] = newProperty.owner
        data[FIELD_PROPERTY_AVAILABLE] = newProperty.available
        data[FIELD_LAT] = newProperty.lat
        data[FIELD_LNG] = newProperty.lng
        data[FIELD_PROPERTY_ID] = newProperty.id

        db.collection(COLLECTION_PROPERTY).document(newProperty.id)
            .update(data) .addOnSuccessListener { docRef ->
                Log.d(TAG, "UpdateProperty: Document successfully updated with ID : $docRef")
            }
            .addOnFailureListener { ex ->
                Log.e(TAG, "UpdateProperty: Exception ocurred while adding a document : $ex", )
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
                data[FIELD_LAT] = newProperty.lat
                data[FIELD_LNG] = newProperty.lng
                data[FIELD_PROPERTY_ID] = newProperty.id


            db.collection(COLLECTION_PROPERTY).document(newProperty.id)
                .set(data)
                .addOnSuccessListener { docRef ->
                    Log.d(TAG, "addPropertyToDB: Document successfully added with ID : $docRef")
                }
                .addOnFailureListener { ex ->
                    Log.e(TAG, "addPropertyToDB: Exception ocurred while adding a document : $ex", )
                }

            }
            catch (ex : java.lang.Exception){
                Log.d(
                    TAG,
                    "addPropertyToDB: Couldn't perform insert on Properties collection due to exception $ex"
                )
            }
        }else{
            Log.e(TAG, "addPropertyToDB: Cannot create Property without user's email address. You must create the account first.", )
        }
    }


}
