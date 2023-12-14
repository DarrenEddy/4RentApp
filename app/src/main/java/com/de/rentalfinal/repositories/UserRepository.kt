package com.de.rentalfinal.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toolbar
import com.de.rentalfinal.R
import com.de.rentalfinal.models.Property
import com.de.rentalfinal.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository(private val context: Context) {
    private val TAG = this.toString()
    private val db = Firebase.firestore

    private val COLLECTION_USERS = "Users"

    private val FIELD_EMAIL = "email"
    private val FIELD_PASSWORD = "password"
    private val FIELD_NAME = "name"
    private val FIELD_TYPE = "type"

    private val prefs = context.getSharedPreferences("com.de.rentalfinal", MODE_PRIVATE)


    fun addUserToDB(newUser : User){
        try{
            val data : MutableMap<String, Any> = HashMap()
            
            data[FIELD_EMAIL] = newUser.email
            data[FIELD_PASSWORD] = newUser.password
            data[FIELD_NAME] = newUser.name
            data[FIELD_TYPE] = newUser.type
            
            db.collection(COLLECTION_USERS)
                .document(newUser.email)
                .set(data)
                .addOnSuccessListener { docRef ->
                    Log.d(TAG, "addUserToDB: User document successfully created with ID $docRef")
                }
                .addOnFailureListener { ex ->
                    Log.e(TAG, "addUserToDB: Unable to create user document due to exception : $ex", )
                }
            
        }catch (ex : Exception){
            Log.e(TAG, "addUserToDB: Couldn't add user document $ex", )
        }
    }

    fun getTypeById(userId: String, onSuccess: (String) -> Unit) {
        try {
            db.collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            onSuccess(user.type)
                        }
                    }
                }
        } catch (ex:java.lang.Exception) {
            Log.d(TAG, "getPropertyById: Could not get property by id due to excepcion: $ex")
        }
    }

}