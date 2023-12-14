package com.de.rentalfinal.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.de.rentalfinal.models.Shortlist
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.UUID

class ShortListRepository(private val context : Context) {
    private val TAG = this.toString();

    //get an instance of firestore database
    private val db = Firebase.firestore

    private val COLLECTION_SHORTLIST = "ShortList";




    fun addToShortList(userId:String, propertyId:String) {
        try {
            db.collection(COLLECTION_SHORTLIST)
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()){
                        //If it is already created
                        val shortList = document.toObject(Shortlist::class.java)
                        if (shortList != null && !shortList.properties.contains(propertyId)){
                            shortList.properties.add(propertyId)
                            //Save the new shortList
                            db.collection(COLLECTION_SHORTLIST).document(userId).set(shortList)
                        }
                    }else {
                        // Created a new one
                        val shortList = Shortlist(mutableListOf(propertyId))
                        db.collection(COLLECTION_SHORTLIST).document(userId).set(shortList)
                    }
                }
        } catch (ex: java.lang.Exception) {
            Log.d(
                TAG,
                "addToShortList: Couldn't perform insert to short list due to exception: $ex"
            )
        }
    }
    fun getListOfProperties(userId: String, onSuccess: (MutableList<String>?) -> Unit) {
        try {
        db.collection(COLLECTION_SHORTLIST)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    val shortlist = document.toObject(Shortlist::class.java)
                    val shortListIds = shortlist?.properties
                    onSuccess(shortListIds)
                }else {
                    onSuccess(mutableListOf())
                }
            }
        }catch (ex : java.lang.Exception){
            Log.e(TAG, "getListOfProperties: Could not retrieve the list of properties due to exception: $ex")
        }
    }
    fun deleteFromShortList(userId: String,propertyId : String){
        try {
            db.collection(COLLECTION_SHORTLIST)
                .document(userId)
                .update("properties", FieldValue.arrayRemove(propertyId))
                .addOnSuccessListener {
                    Log.d(TAG, "deleteFromShortList: Property: $propertyId deleted successfully")
                }
        } catch (ex: Exception) {
            Log.e(TAG, "deleteCountry: Exception occurred: $ex")
        }
    }


}