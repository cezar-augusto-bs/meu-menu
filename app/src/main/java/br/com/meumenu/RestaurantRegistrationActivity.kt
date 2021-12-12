package br.com.meumenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.meumenu.model.Restaurant
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurant_regitration.*

class RestaurantRegistrationActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_regitration)

        btn_salvarRestaurante.setOnClickListener {
            if(txt_nomeRestaurante.text.toString().isNotEmpty()){
                //writeNewRestaurant()
            }else{

            }
        }
    }
    fun writeNewRestaurant(restaurant: Restaurant) {
        val nomeRestaurante = txt_nomeRestaurante

        val restaurante = Restaurant()
        restaurante.name = nomeRestaurante.toString()

        database.reference.child("restaurants").setValue(restaurant)
    }
}