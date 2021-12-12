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

        btn_irCadastro.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btn_logar.setOnClickListener {
            val email = txt_emailLogin.text.toString();
            val password = txt_senhaLogin.text.toString();

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