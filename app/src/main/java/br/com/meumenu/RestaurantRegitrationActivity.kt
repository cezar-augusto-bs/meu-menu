package br.com.meumenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.meumenu.model.Restaurant
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_restaurant_regitration.*

class RestaurantRegitrationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_regitration)

        database = Firebase.database.reference

        btn_salvarRestaurante.setOnClickListener {
            if(txt_nomeRestaurante.text.toString().isNotEmpty()){
                writeNewRestaurant()
            }else{

            }
        }
    }
    fun writeNewRestaurant(restaurant: Restaurant) {
        val nomeRestaurante = txt_nomeRestaurante

        val restaurante = Restaurant()
        restaurante.name = nomeRestaurante.toString()

        database.child("restaurants").child(restId).setValue(restaurant)
    }
}