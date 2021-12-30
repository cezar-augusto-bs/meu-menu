package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.adapter.MenuAdapter
import br.com.meumenu.adapter.RestaurantAdapter
import br.com.meumenu.model.Menu
import br.com.meumenu.model.Restaurant
import br.com.meumenu.util.AuthUtil
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_menu_list.*
import java.lang.Exception

class   MenuListActivity : AppCompatActivity() {
    private val TAG = "MENU-LIST"

    private val database = FirebaseDatabase.getInstance()
    private lateinit var currentUser: String
    private lateinit var restaurantId: String

    private lateinit var itemMenuRecyclerView: RecyclerView
    private lateinit var itemMenuAdapter: MenuAdapter
    private lateinit var itemMenuList: ArrayList<Menu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        try {
            currentUser = AuthUtil.getCurrentUser().toString()
            restaurantId = getRestaurantId()

            initItemMenuRecyclerView()
            setItemMenuRecyclerView()
            configureButtonsOnClickListener()
        }catch (exception : Exception) {
            Log.e(TAG, "Exception: $exception")
        }
    }

    private fun getRestaurantId(): String {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            return bundle.getString("restaurantId").toString()
        }else {
            finish()
        }
        return ""
    }

    private fun initItemMenuRecyclerView() {
        itemMenuRecyclerView = findViewById(R.id.menu_list)
        itemMenuRecyclerView.layoutManager = LinearLayoutManager(this)
        itemMenuRecyclerView.setHasFixedSize(true)
    }

    private fun setItemMenuRecyclerView() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setItemMenuList(dataSnapshot)
                setItemMenuAdapter()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", "Error load items menu: $databaseError")
            }
        }

        database
            .getReference("itemRestaurant")
            .orderByChild("restaurantId")
            .equalTo(restaurantId)
            .addValueEventListener(menuListener)
    }

    private fun setItemMenuList(dataSnapshot: DataSnapshot) {
        itemMenuList = arrayListOf<Menu>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                val itemMenu = snapshot.getValue<Menu>()
                if (itemMenu != null) {
                    itemMenu.id = snapshot.key
                    itemMenuList.add(itemMenu)
                }
            }
        }
    }

    private fun setItemMenuAdapter(){
        itemMenuAdapter = MenuAdapter(itemMenuList)
        itemMenuRecyclerView.adapter = itemMenuAdapter
    }

    private fun configureButtonsOnClickListener() {
        btn_add_prato_arroz.setOnClickListener {
            val intent = Intent(this, MenuRegistrationActivity::class.java)
            intent.putExtra("restaurantId", restaurantId)
            startActivity(intent)
        }
    }
}