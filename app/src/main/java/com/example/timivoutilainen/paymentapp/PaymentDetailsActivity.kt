package com.example.timivoutilainen.paymentapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView

class PaymentDetailsActivity : AppCompatActivity() {
    lateinit var nameTextView: TextView
    lateinit var amountTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var extras: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameTextView = findViewById(R.id.nameTextView)
        amountTextView = findViewById(R.id.amountTextView)
        dateTextView = findViewById(R.id.dateTextView)

        extras = intent.extras

        setTextViews()
    }

    private fun setTextViews() {
        if (extras != null) {
            nameTextView.text = extras.getString("name")
            amountTextView.text = extras.getString("amount")
            dateTextView.text = extras.getString("date")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}
