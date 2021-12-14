package br.com.meumenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.com.meumenu.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurant_regitration.*
import android.R.id

import android.content.Intent




class RestaurantRegistrationActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_regitration)

        btn_salvarRestaurante.setOnClickListener {
            val restaurant = setRestaurantData()
            val nomeRestaurant = txt_nomeRestaurante

            if(txt_nomeRestaurante.text.toString().isNotEmpty()){
                writeNewRestaurant(restaurant)
                startActivity(Intent(this, RestaurantListActivity::class.java))
            }else{
                txt_nomeRestaurante.error = "Voce precisa colocar um nome para o restaurante"
            }

        }
    }
    private fun setRestaurantData(): Restaurant {
        val nomeRestaurante = txt_nomeRestaurante.text.toString()

        val restaurant = Restaurant()
        restaurant.name = nomeRestaurante

        return restaurant
    }

    private fun writeNewRestaurant(restaurant: Restaurant) {
        database.getReference("restaurants").setValue(restaurant)
    }
    private fun getCurrentUser(): String? {
        val user = auth.currentUser;
        return if (user !== null)
            user.uid;
        else
            null;
    }
}