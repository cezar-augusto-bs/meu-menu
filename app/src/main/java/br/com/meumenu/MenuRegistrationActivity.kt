package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu_list.*
import kotlinx.android.synthetic.main.activity_menu_registration.*

class MenuRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_registration)


        val spnPratoCategoria = arrayOf("Entrada", "Pratos", "Bebidas", "Bebidas alcoolicas", "Sobremesa")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spnPratoCategoria)

        spn_cadastro_prato_categoria.adapter = arrayAdapter

        var pratoCategoria : String = ""

        spn_cadastro_prato_categoria.onItemSelectedListener = object :

        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pratoCategoria = spnPratoCategoria[p2]
                Toast.makeText(this@MenuRegistrationActivity, "$pratoCategoria", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        btn_add_prato.setOnClickListener{
            startActivity(Intent(this, MenuListActivity::class.java))
        }
    }
}