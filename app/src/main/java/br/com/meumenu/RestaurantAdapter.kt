package br.com.meumenu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import br.com.meumenu.model.Restaurant

class RestaurantAdapter(contexto:Context) : ArrayAdapter<Restaurant>(contexto, 1){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View

        if(convertView != null){
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false)
        }

        val item = getItem(position)

        val restaurantName = v.findViewById<TextView>(R.id.txt_restautant_name)
        //val restaurantImg = v.findViewById<ImageView>(R.id.img_restaurant_photo)

        restaurantName.text = item?.name

        /*if(item?.photo != null)
            restaurantImg.setImageBitMap(item.photo)*/


        return v
    }
}