package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.model.Menu
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_menu_list.*
import kotlinx.android.synthetic.main.restaurant_item.*

class MenuListActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuArraylist: ArrayList<Menu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_menu_list)

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

    private fun getMenuData() {
        dbref = FirebaseDatabase.getInstance().getReference("Menu")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (snapshot in dataSnapshot.children){
                        val menu = dataSnapshot.getValue<Menu>()
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
        dbref.addValueEventListener(menuListener)
    }
}