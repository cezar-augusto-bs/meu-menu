package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import kotlinx.android.synthetic.main.restaurant_item.*
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener







class RestaurantListActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    var reference = FirebaseDatabase.getInstance().reference

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

        btn_ir_menu_list2.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }

    }


    private fun getRestaurantData() {
            val restaurantListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val restaurant = snapshot.getValue<Restaurant>()
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
            database.reference.child("restaurants").addValueEventListener(restaurantListener)

    }

    /*private fun getRestaurantData(){
        val query = reference.child("restaurants").orderByChild("userId").equalTo(getCurrentUser().toString())
        val restaurantListener = query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val restaurant = snapshot.getValue<Restaurant>()
                        if (restaurant != null) {
                            restaurantArraylist.add(restaurant)
                        }
                    }
                    restaurantRecyclerView.adapter = RestaurantAdapter(restaurantArraylist)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                
            }
        })
        database.reference.child("restaurants").addValueEventListener(restaurantListener)
    }*/



    private fun getCurrentUser(): String? {
        val user = auth.currentUser;
        return if (user !== null)
            user.uid;
        else
            null;
    }
}