package br.com.meumenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.model.Restaurant

class RestaurantAdapter(private val restaurantList : ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = restaurantList[position]

        holder.name.text = currentItem.name
        //holder.profilePic. = currentItem.profilePic
    }

    override fun getItemCount(): Int {

        return restaurantList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.txt_nameRestaurantItem)
        //val profilePic : ImageView = itemView.findViewById(R.id.img_restaurant)
    }
}