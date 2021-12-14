package br.com.meumenu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signup_btn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        signin.setOnClickListener {
            val email = email_login.text.toString();
            val password = password_login.text.toString();

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, RestaurantListActivity::class.java))
                    } else {
                        Toast.makeText(this@MainActivity, "Login inválido", Toast.LENGTH_LONG).show()
                    };
                }
        }
    }
}