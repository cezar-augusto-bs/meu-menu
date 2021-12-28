package br.com.meumenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import br.com.meumenu.model.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_menu_list.*
import kotlinx.android.synthetic.main.activity_menu_registration.*

class MenuRegistrationActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_registration)


        val spnPratoCategoria = arrayOf("Entrada", "Pratos", "Bebidas", "Bebidas alcoolicas", "Sobremesa")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spnPratoCategoria)
        var pratoCategoria : String = ""

        spn_cadastro_prato_categoria.adapter = arrayAdapter

        spn_cadastro_prato_categoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pratoCategoria = spnPratoCategoria[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        btn_salvar_prato.setOnClickListener {
            val prato = setPratoData(pratoCategoria)

            if(txt_cadastro_prato_descricao.text.toString().isNotEmpty() && txt_cadastro_prato_nome.text.toString().isNotEmpty() && txt_cadastro_prato_preco.text.toString().isNotEmpty() && spn_cadastro_prato_categoria.isNotEmpty()){
                writeNewPrato(prato)
                startActivity(Intent(this, MenuListActivity::class.java))
            }else{
                Toast.makeText(this, "Todos os valores sao obrigatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPratoData(categoria : String) : Menu {

        var nomePrato = txt_cadastro_prato_nome.text.toString()
        var precoPrato = txt_cadastro_prato_preco.text.toString()
        var descricao = txt_cadastro_prato_descricao.text.toString()

        var menu = Menu()
        menu.nome = nomePrato
        menu.descricao = descricao
        menu.preco = precoPrato.toFloat()
        menu.categoria = categoria

        return menu
    }

    private fun writeNewPrato(menu : Menu){
        database.getReference().child("menu").push().setValue(menu)
    }

    private fun getCurrentUser(): String? {
        val user = auth.currentUser;
        return if (user !== null)
            user.uid;
        else
            null;
    }
}