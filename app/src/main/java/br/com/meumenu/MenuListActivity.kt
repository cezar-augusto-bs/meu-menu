package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.adapter.MenuAdapter
import br.com.meumenu.model.Menu
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_menu_list.*

class MenuListActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuArraylist: ArrayList<Menu>

    private lateinit var restaurantId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_menu_list)

            getRestaurantId()

            menuRecyclerView = findViewById(R.id.menu_list)
            menuRecyclerView.layoutManager = LinearLayoutManager(this)
            menuRecyclerView.setHasFixedSize(true)

            menuArraylist = ArrayList<Menu>()
            getMenuData()

            btn_add_prato_arroz.setOnClickListener {
                startActivity(Intent(this, MenuRegistrationActivity::class.java))
            }
        } catch(error: Exception) {
            var res = error
        }
    }

    private fun getRestaurantId() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            restaurantId = bundle.getString("restaurantId").toString()
        }else {
            finish()
        }
    }

    private fun getMenuData() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (snapshot in dataSnapshot.children){
                        val menu = snapshot.getValue<Menu>()
                        if (menu != null) {
                            menuArraylist.add(menu)
                        }
                    }
                    menuRecyclerView.adapter = MenuAdapter(menuArraylist)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database
            .getReference("menu")
            .orderByChild("restaurantId")
            .equalTo(restaurantId)
            .addValueEventListener(menuListener)
    }
}