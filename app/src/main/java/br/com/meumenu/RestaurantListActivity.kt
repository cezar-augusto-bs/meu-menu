package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private val TAG = "RESTAURANT-LIST"

    private val database = FirebaseDatabase.getInstance()
    private lateinit var currentUser: String

    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantList: ArrayList<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        try {

            currentUser = AuthUtil.getCurrentUser().toString()

            initRestaurantRecyclerView()
            setRestaurantRecyclerView()
            configureButtonsOnClickListener()
        }catch (exception : Exception) {
            Log.e(TAG, "Exception: $exception")
        }
    }

    private fun initRestaurantRecyclerView() {
        restaurantRecyclerView = findViewById(R.id.restaurant_list)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.setHasFixedSize(true)
    }

    private fun setRestaurantRecyclerView() {
        val restaurantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setRestaurantList(dataSnapshot)
                setRestaurantAdapter()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", "Error load restaurant: $databaseError")
            }
        }

        database
            .getReference("restaurant")
            .orderByChild("userId")
            .equalTo(currentUser)
            .addValueEventListener(restaurantListener)
    }

    private fun setRestaurantList(dataSnapshot: DataSnapshot) {
        restaurantList = arrayListOf<Restaurant>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                val restaurant = snapshot.getValue<Restaurant>()
                if (restaurant != null) {
                    restaurant.id = snapshot.key
                    restaurantList.add(restaurant)
                }
            }
        }
    }

    private fun setRestaurantAdapter(){
        restaurantAdapter = RestaurantAdapter(restaurantList)
        restaurantRecyclerView.adapter = restaurantAdapter
        setItemOnClickListenerRecyclerView()
    }

    private fun setItemOnClickListenerRecyclerView() {
        restaurantAdapter.setOnItemClickListener(object : ClickListener {
            override fun onItemClick(v: View, position: Int) {
                val selectedAdapterItem = restaurantAdapter.getItem(position)
                goToRestaurantMenu(selectedAdapterItem)
            }
        })
    }

    private fun goToRestaurantMenu(restaurant : Restaurant) {
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("restaurantId", restaurant.id)
        startActivity(intent)
    }

    private fun configureButtonsOnClickListener() {
        btn_add_restaurant.setOnClickListener {
            startActivity(Intent(this, RestaurantRegistrationActivity::class.java))
        }
    }
}