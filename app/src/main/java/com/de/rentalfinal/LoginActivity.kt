package com.de.rentalfinal

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.de.rentalfinal.databinding.ActivityLoginBinding
import com.de.rentalfinal.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        this.userRepository = UserRepository(applicationContext)
        this.prefs = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        this.binding.btnSignIn.setOnClickListener(this)
        this.binding.btnSignUp.setOnClickListener(this)

        setContentView(this.binding.root)
    }

    override fun onClick(p0: View?) {
        when (p0?.id)
        {
            R.id.btn_sign_in ->
                {
                    val email = this.binding.editEmail.text
                    val password = this.binding.editPassword.text

                    if (email.isNullOrEmpty() || password.isNullOrEmpty())
                    {
                        if (email.isNullOrEmpty()){this.binding.editEmail.setError("Cannot Be Empty")}
                        if (password.isNullOrEmpty()){this.binding.editPassword.setError("Cannot Be Empty")}
                    }
                    else
                    {
                        this.firebaseAuth.signInWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener {task->
                            if (task.isSuccessful)
                            {
                                saveToPrefs(email.toString())
                                goToMain()
                            }
                            else
                            {
                                Toast.makeText(this, "Unable To Login", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                }
            R.id.btn_sign_up ->
                {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
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