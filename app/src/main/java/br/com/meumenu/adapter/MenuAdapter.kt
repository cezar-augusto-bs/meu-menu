package br.com.meumenu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.R
import br.com.meumenu.model.Menu

class MenuAdapter(private val menuList : ArrayList<Menu>) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = menuList[position]

        holder.nomePrato.text = currentItem.name
        holder.precoPrato.text = (currentItem.price).toString()
        holder.descricaoPrato.text = currentItem.description
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nomePrato : TextView = itemView.findViewById(R.id.txt_nome_prato)
        val precoPrato : TextView = itemView.findViewById(R.id.txt_preco_prato)
        val descricaoPrato : TextView = itemView.findViewById(R.id.txt_descricao_prato)
        val imgPrato : ImageView = itemView.findViewById(R.id.img_prato)
    }
}