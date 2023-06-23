package com.example.starshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize


class LoginActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    val Req_Code:Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signing : SignInButton
    private lateinit var login : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Firebase.initialize(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        firebaseAuth = FirebaseAuth.getInstance()

        signing = findViewById(R.id.sign_in_button)
        login = findViewById(R.id.login)

        signing.setOnClickListener{ view: View? ->
            Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show()
            signInToGoogle()
        }

        login.setOnClickListener{ view: View?->
            signInWithPass()

        }

    }

    private fun signInWithPass()
    {
        try {
            firebaseAuth.createUserWithEmailAndPassword(user.text.toString(), pass.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Error)
        {
            if (e.message == "auth/email-already-in-use")
            {
                firebaseAuth.signInWithEmailAndPassword(user.text.toString(), pass.text.toString()).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        SavedPreference.setEmail(this, user.text.toString())
                        SavedPreference.setPass(this, pass.text.toString())
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }


    private fun signInToGoogle()
    {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null)
            {
                updateUI(account)
            }

        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount)
    {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task->
            if (task.isSuccessful){
                SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val user: FirebaseUser? = firebaseAuth.currentUser
            user?.let {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "welcome back", Toast.LENGTH_SHORT).show()
            }
        }
    }

}