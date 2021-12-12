package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_restaurant.*

class RestaurantListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_restaurant)

        btn_irCadastrar2.setOnClickListener {
            startActivity(Intent(this, RestaurantRegitrationActivity::class.java))
        }
    }
}