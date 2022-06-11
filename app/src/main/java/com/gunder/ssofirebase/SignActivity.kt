package com.gunder.ssofirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gunder.ssofirebase.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    private lateinit var signBinding: ActivitySignBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signBinding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(signBinding.root)
        supportActionBar?.hide()
        requestSign()
    }

    private fun requestSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        signBinding.btnSignGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signIntent = googleSignClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onActivityResult(requestCode, resultCode, data)",
            "androidx.appcompat.app.AppCompatActivity"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "Firebase Auth With Google" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Sign with Google failed!")
                }
            } else {
                Log.w(TAG, exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signWithCredentialSuccessfull")
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signWithCredentialFailed", task.exception)
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "signActivity"
    }
}