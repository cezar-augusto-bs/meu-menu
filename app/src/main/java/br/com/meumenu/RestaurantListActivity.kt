package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_restaurant_list.*

class RestaurantListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val adapter = RestaurantAdapter(this)

        //restaurants_list.adapter = adapter


        btn_add_restaurant.setOnClickListener {
            startActivity(Intent(this, RestaurantRegistrationActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        //val adapter = restaurants_list.adapter as RestaurantAdapter
        //adapter.clear()
        //adapter.addAll(restaurantsGlobal)
    }
}