package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.model.Restaurant
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_restaurant_list.*

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantArraylist: ArrayList<Restaurant>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        restaurantRecyclerView = findViewById(R.id.restaurant_list)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.setHasFixedSize(true)


        restaurantArraylist = arrayListOf<Restaurant>()
        getRestaurantData()

        btn_add_restaurant.setOnClickListener {
            startActivity(Intent(this, RestaurantRegistrationActivity::class.java))
        }
    }

    private fun getRestaurantData() {

        dbref = FirebaseDatabase.getInstance().getReference("restaurants")

        val restaurantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (snapshot in dataSnapshot.children){
                        val restaurant = dataSnapshot.getValue<Restaurant>()
                        if (restaurant != null) {
                            restaurantArraylist.add(restaurant)
                        }
                    }
                    restaurantRecyclerView.adapter = RestaurantAdapter(restaurantArraylist)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        dbref.addValueEventListener(restaurantListener)
    }
}