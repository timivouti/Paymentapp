package com.example.timivoutilainen.paymentapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter(private val paymentsList: ArrayList<PaymentModel>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val year = paymentsList[position].transaction_date.substring(0, 4).toInt()
        val month = paymentsList[position].transaction_date.substring(5, 7).toInt()
        val day = paymentsList[position].transaction_date.substring(8, 10).toInt()

        holder.paymentName.text = paymentsList[position].name
        holder.paymentAmount.text = paymentsList[position].amount.toString()
        holder.paymentDate.text = SimpleDateFormat("dd.MM.yyyy").format(GregorianCalendar(year, month, day).time).toString()

        if (paymentsList[position].amount < 0) {
            holder.paymentAmount.setTextColor(Color.parseColor("#ff9999"))
        } else {
            holder.paymentAmount.setTextColor(Color.parseColor("#99ff99"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.recyclerview_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return paymentsList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val paymentName = itemView.findViewById<TextView>(R.id.paymentName)!!
        val paymentAmount = itemView.findViewById<TextView>(R.id.paymentAmount)!!
        val paymentDate = itemView.findViewById<TextView>(R.id.paymentDate)!!
    }
}

