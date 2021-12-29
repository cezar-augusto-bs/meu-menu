package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.`interface`.ClickListener
import br.com.meumenu.adapter.RestaurantAdapter
import br.com.meumenu.model.Restaurant
import br.com.meumenu.util.AuthUtil
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

class RestaurantListActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var currentUser: String
    private lateinit var restaurantsRecyclerView: RecyclerView

    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var listRestaurants: ArrayList<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        currentUser = AuthUtil.getCurrentUser().toString()

        initRestaurantListRecyclerView()
        setRestaurantListRecyclerView()
        setRedirectButtonsOnClickListener()
    }

    private fun initRestaurantListRecyclerView() {
        restaurantsRecyclerView = findViewById(R.id.restaurant_list)
        restaurantsRecyclerView.layoutManager = LinearLayoutManager(this)
        restaurantsRecyclerView.setHasFixedSize(true)
    }

    private fun setRestaurantListRecyclerView() {
        val restaurantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var listRestaurants: ArrayList<Restaurant> = createListRestaurants(dataSnapshot)
                setListRestaurantsAdapter(listRestaurants)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", "Error load restaurants: $databaseError")
            }
        }

        database
            .getReference("restaurants")
            .orderByChild("userId")
            .equalTo(currentUser)
            .addValueEventListener(restaurantListener)
    }

    private fun createListRestaurants(dataSnapshot: DataSnapshot) : ArrayList<Restaurant> {
        listRestaurants = arrayListOf<Restaurant>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                val restaurant = snapshot.getValue<Restaurant>()
                if (restaurant != null) {
                    restaurant.id = snapshot.key
                    listRestaurants.add(restaurant)
                }
            }
        }
        return listRestaurants
    }

    private fun setListRestaurantsAdapter(listRestaurants : ArrayList<Restaurant>){
        restaurantAdapter = RestaurantAdapter(listRestaurants)
        restaurantsRecyclerView.adapter = restaurantAdapter
        setItemOnClickListenerRecyclerView()
    }

    private fun setItemOnClickListenerRecyclerView() {
        restaurantAdapter.setOnItemClickListener(object : ClickListener {
            override fun onItemClick(v: View, position: Int) {
                var selectedAdapterItem = restaurantAdapter.getItem(position)
                goToRestaurantMenu(selectedAdapterItem)
            }
        })
    }

    private fun goToRestaurantMenu(restaurant : Restaurant) {
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("restaurantId", restaurant.id)
        startActivity(intent)
    }

    private fun setRedirectButtonsOnClickListener() {
        btn_add_restaurant.setOnClickListener {
            startActivity(Intent(this, RestaurantRegistrationActivity::class.java))
        }

        btn_ir_menu_list2.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }
    }
}