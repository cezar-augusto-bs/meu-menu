package br.com.meumenu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.meumenu.R
import br.com.meumenu.`interface`.ClickListener
import br.com.meumenu.model.Restaurant

class RestaurantAdapter(private val restaurantList: ArrayList<Restaurant>)
    : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val name: TextView = view.findViewById(R.id.txt_nameRestaurantItem)

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        fun bindItems(restaurant: Restaurant) {
            name.text = restaurant.name
        }

        override fun onClick(view: View?) {
            if (view != null) {
                clickListener?.onItemClick(view, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.restaurant_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = restaurantList[position]
        data.let { holder.bindItems(it) }
    }

    fun getItem(position: Int): Restaurant {
        return restaurantList[position]
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }
}