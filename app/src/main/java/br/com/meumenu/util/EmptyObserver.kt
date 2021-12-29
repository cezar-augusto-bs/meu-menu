package br.com.meumenu.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyObserver(private val rv: RecyclerView, private val ev: View) : RecyclerView.AdapterDataObserver() {

    init {
        checkEmpty()
    }

    private fun checkEmpty() {
        if (rv.adapter!!.itemCount == 0) {
            ev.visibility = View.VISIBLE
            rv.visibility = View.GONE
        }else {
            ev.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }

    override fun onChanged() {
        super.onChanged()
        checkEmpty()
    }
}