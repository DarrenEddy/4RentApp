package com.de.rentalfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.de.rentalfinal.databinding.ActivityLoginBinding
import com.de.rentalfinal.databinding.ActivitySignUpBinding
import com.de.rentalfinal.models.User
import com.de.rentalfinal.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity(), OnClickListener {
    private val TAG = this.javaClass.canonicalName
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    private var accountSwitch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivitySignUpBinding.inflate(layoutInflater)
        this.binding.switchAccountType.setOnClickListener(this)
        this.binding.btnCreateAccount.setOnClickListener(this)
        this.userRepository = UserRepository(applicationContext)
        this.firebaseAuth = FirebaseAuth.getInstance()

        setContentView(this.binding.root)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.switchAccountType -> {
                accountSwitch = !accountSwitch
            }

            R.id.btn_create_account -> {
                if (this.binding.editEmail.text.isNullOrEmpty() || this.binding.editPassword.text.isNullOrEmpty() || this.binding.editConfirmPassword.text.isNullOrEmpty()) {

                    if (this.binding.editEmail.text.isNullOrEmpty()) {
                        this.binding.editEmail.setError("Cannot Be Empty")
                    }
                    if (this.binding.editPassword.text.isNullOrEmpty()) {
                        this.binding.editPassword.setError("Cannot Be Empty")
                    }
                    if (this.binding.editConfirmPassword.text.isNullOrEmpty()) {
                        this.binding.editConfirmPassword.setError("Cannot Be Empty")
                    }
                } else {
                    val email = this.binding.editEmail.text.toString()
                    val password = this.binding.editPassword.text.toString()

                    if (password != this.binding.editConfirmPassword.text.toString()) {
                        this.binding.editPassword.setError("Passwords don't match")
                        this.binding.editConfirmPassword.setError("Passwords don't match")
                    } else {
                        //  Create Account
                        this.firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->

                                if (task.isSuccessful) {
                                    var type = "Tenant"
                                    if (accountSwitch) {
                                        type = "Landlord"
                                    }

                                    var name = "Unknown"
                                    if (this.binding.editName.text.isNotEmpty()) {
                                        name = this.binding.editName.text.toString()
                                    }

                                    val user = User(
                                        email = email,
                                        password = password,
                                        name = name,
                                        type = type
                                    )
                                    userRepository.addUserToDB(user)
                                    saveToPrefs(email)
                                    goToMain()
                                } else {
                                    Log.d(
                                        TAG,
                                        "createAccount: Unable to create user account : ${task.exception}",
                                    )
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Account creation failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    }
                }
            }

        }
    }

    private fun saveToPrefs(email: String) {
        val prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        prefs.edit().putString("USER_EMAIL", email).apply()
    }

    private fun goToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

}