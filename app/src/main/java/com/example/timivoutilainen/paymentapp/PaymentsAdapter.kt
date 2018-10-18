package com.example.timivoutilainen.paymentapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter(private val paymentsList: ArrayList<PaymentModel>, private val context: Context): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val year = paymentsList[position].transaction_date.substring(0, 4).toInt()
        val month = paymentsList[position].transaction_date.substring(5, 7).toInt()
        val day = paymentsList[position].transaction_date.substring(8, 10).toInt()
        val date = SimpleDateFormat("dd.MM.yyyy").format(GregorianCalendar(year, month, day).time).toString()

        holder.paymentName.text = paymentsList[position].name
        holder.paymentAmount.text = paymentsList[position].amount.toString()
        holder.paymentDate.text = date
        holder.cardView.setOnClickListener {
            val intent = Intent(context, PaymentDetailsActivity::class.java).apply {
                putExtra("name", paymentsList[position].name)
                putExtra("amount", paymentsList[position].amount.toString())
                putExtra("date", date)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(context, intent, null)
        }

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
        val cardView = itemView.findViewById<CardView>(R.id.cardView)!!
    }
}

