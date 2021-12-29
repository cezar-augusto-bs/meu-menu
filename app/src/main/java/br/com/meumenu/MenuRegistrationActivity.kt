package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import br.com.meumenu.model.Category
import br.com.meumenu.model.Menu
import br.com.meumenu.model.Restaurant
import br.com.meumenu.util.AuthUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_menu_registration.*
import java.lang.Exception

class MenuRegistrationActivity : AppCompatActivity() {
    private val TAG = "MENU-REGISTRATION"

    private val database = FirebaseDatabase.getInstance()
    private lateinit var currentUserId: String
    private lateinit var restaurantId: String

    private lateinit var categoryAdapter: ArrayAdapter<Category>
    private lateinit var categoryList: ArrayList<Category>
    private var selectedCategory: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_registration)

        try {
            currentUserId = AuthUtil.getCurrentUser().toString()
            restaurantId = getRestaurantId()
            setCategorySpinner()

            btn_salvar_prato.setOnClickListener {
                val itemMenu = createObjectItemMenu()

                if (txt_cadastro_prato_descricao.text.toString().isNotEmpty()
                    && txt_cadastro_prato_nome.text.toString().isNotEmpty()
                    && txt_cadastro_prato_preco.text.toString().isNotEmpty()
                    && spn_cadastro_prato_categoria.isNotEmpty()
                ) {
                    saveNewItemMenu(itemMenu)
                    goToMenuList()
                } else {
                    Toast.makeText(this, "Todos os valores sao obrigatorios", Toast.LENGTH_SHORT)
                        .show()
                }
            }
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

    private fun setCategorySpinner() {
        val categoryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setCategoryList(dataSnapshot)
                setCategoryAdapter()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", "Error load categories: $databaseError")
            }
        }

        database
            .getReference("category")
            .addValueEventListener(categoryListener)
    }

    private fun setCategoryList(dataSnapshot: DataSnapshot) {
        categoryList = arrayListOf<Category>()

        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                val category = snapshot.getValue<Category>()
                if (category != null) {
                    category.id = snapshot.key
                    categoryList.add(category)
                }
            }
        }
    }

    private fun setCategoryAdapter() {
        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        spn_cadastro_prato_categoria.adapter = categoryAdapter
        setCategoryOnItemSelectedListener()
    }

    private fun setCategoryOnItemSelectedListener() {
        spn_cadastro_prato_categoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun createObjectItemMenu() : Menu {
        val name = txt_cadastro_prato_nome.text.toString()
        val price = txt_cadastro_prato_preco.text.toString()
        val description = txt_cadastro_prato_descricao.text.toString()

        var itemMenu = Menu()
        itemMenu.restaurantId = restaurantId
        itemMenu.userId = currentUserId
        itemMenu.categoryId = selectedCategory?.toInt()
        itemMenu.name = name
        itemMenu.description = description
        itemMenu.price = price.toFloat()

        return itemMenu
    }

    private fun saveNewItemMenu(itemMenu : Menu){
        database.getReference("itemRestaurant").push().setValue(itemMenu)
    }

    private fun goToMenuList() {
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("restaurantId", restaurantId)
        startActivity(intent)
    }
}