package com.de.rentalfinal.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toolbar
import com.de.rentalfinal.R
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

    fun getTypeByEmail(email: String, menu: androidx.appcompat.widget.Toolbar)
    {
        db.collection(COLLECTION_USERS).document(email).get().addOnSuccessListener {
            val type = it.get(FIELD_TYPE).toString()
            prefs.edit().putString("USER_TYPE", type).apply()
            adjustMenu(type,menu)
            Log.d(TAG,"UserRepository: got $email type of $type")

        }.addOnFailureListener{
            Log.e(TAG,"UserRepository: Error getting Type By email $it")
        }
    }

    private fun adjustMenu(type:String, menu: androidx.appcompat.widget.Toolbar)
    {
        if (type == "Landlord")
        {
            menu.menu.clear()
            menu.inflateMenu(R.menu.menu_options_landlord)
        }
    }

//    fun updateUserProfile(userToUpdate : User){
//        try{
//            val data : MutableMap<String, Any> = HashMap()
//
//            data[FIELD_PASSWORD] = userToUpdate.password
//            data[FIELD_PHONE] = userToUpdate.phoneNumber
//            data[FIELD_NAME] = userToUpdate.name
//
//            db.collection(COLLECTION_USERS)
//                .document(userToUpdate.email)
//                .update(data)
//                .addOnSuccessListener { docRef ->
//                    Log.d(TAG, "updateUserProfile: User document successfully updated $docRef")
//                }
//                .addOnFailureListener { ex ->
//                    Log.e(TAG, "updateUserProfile: Unable to update user document due to exception : $ex", )
//                }
//
//        }catch (ex : Exception){
//            Log.e(TAG, "updateUserProfile: Couldn't update user document $ex", )
//        }
//    }
}