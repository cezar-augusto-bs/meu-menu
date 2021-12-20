package br.com.meumenu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.meumenu.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        cancel.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        signup.setOnClickListener {
            val user = setUserData()

            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    writeUserDatabase(user)
                    Toast.makeText(this@SignupActivity, "Usuario Cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this@SignupActivity, "Error: Insira um email valido!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun setUserData() : User {
        val email = email.text.toString();
        val name = name.text.toString();
        val password = password.text.toString();

        val user = User()
        user.email = email;
        user.name = name;
        user.password = password;
        return user;
    }

    private fun writeUserDatabase(user: User) {
        val userUid = getCurrentUser()
        val child = "users/$userUid"
        database.reference.child(child).setValue(user)
    }

    private fun getCurrentUser(): String? {
        val user = auth.currentUser;
        return if (user !== null)
            user.uid;
        else
            null;
    }
}