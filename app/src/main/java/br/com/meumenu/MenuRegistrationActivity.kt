package br.com.meumenu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import br.com.meumenu.model.Category
import br.com.meumenu.model.Menu
import br.com.meumenu.util.AuthUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu_registration.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import android.os.Build
import java.io.IOException
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage


class MenuRegistrationActivity : AppCompatActivity() {
    private val TAG = "MENU-REGISTRATION"
    private val PICK_PHOTO_CODE = 1046
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var photoUri: Uri? = null

    private lateinit var currentUserId: String
    private lateinit var restaurantId: String
    private lateinit var itemRestaurantId: String

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

            image_item_menu.setOnClickListener {
                requestRead()
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

        val itemMenu = Menu()
        itemMenu.restaurantId = restaurantId
        itemMenu.userId = currentUserId
        itemMenu.categoryId = selectedCategory?.toInt()
        itemMenu.name = name
        itemMenu.description = description
        itemMenu.price = price.toFloat()

        return itemMenu
    }

    private fun saveNewItemMenu(itemMenu : Menu){
        val key = database.reference.child("itemRestaurant").push().key
        if (key == null) {
            Log.w(TAG, "Não foi possivel gerar uma nova chave para o item do restaurant")
            return
        }
        itemRestaurantId = key

        database.getReference("itemRestaurant/$key").setValue(itemMenu)
        saveItemMenuImage()
    }

    private fun saveItemMenuImage() {
        val storageReference = storage.getReference("itemRestaurant/${itemRestaurantId}")
        storageReference.putFile(photoUri!!)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        updateItemMenu(downloadUri.toString())
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao salvar imagem do item do menu!", Toast.LENGTH_SHORT).show()
                    }
            }
    }

    private fun updateItemMenu(downloadUri: String) {
        database
            .getReference("itemRestaurant/$itemRestaurantId/imageSrc")
            .setValue(downloadUri)
    }

    private fun goToMenuList() {
        val intent = Intent(this, MenuListActivity::class.java)
        intent.putExtra("restaurantId", restaurantId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == PICK_PHOTO_CODE && resultCode == Activity.RESULT_OK && data != null) {
                photoUri = data.data!!

                val byteArrayOutputStream = ByteArrayOutputStream()
                loadFromUri(photoUri)?.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                )
                Picasso.get().load(photoUri).resize(150, 150).centerCrop()
                    .into(image_item_menu)
            }
        }catch (exception : Exception) {
            Log.e(TAG, "Exception: $exception")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MenuRegistrationActivity, "Permissão para acessar galeria negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestRead() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }else {
            pickPhoto()
        }
    }

    private fun pickPhoto() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }

    private fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, photoUri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }
}