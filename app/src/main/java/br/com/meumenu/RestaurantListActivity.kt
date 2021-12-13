package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.model.Restaurant
import com.google.firebase.database.*
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

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    for (restSnapshot in snapshot.children){

                        val restaurant = restSnapshot.getValue(Restaurant::class.java)
                        restaurantArraylist.add(restaurant!!)

                    }

                    restaurantRecyclerView.adapter = RestaurantAdapter(restaurantArraylist)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}