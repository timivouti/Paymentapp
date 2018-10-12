package com.example.timivoutilainen.paymentapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddPaymentActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    lateinit var amountText: EditText
    lateinit var receiverNameText: EditText
    lateinit var receiverNumberText: EditText
    private val paymentServe by lazy { PaymentService.create() }
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        amountText = findViewById(R.id.amountText)
        receiverNameText = findViewById(R.id.receiverNameText)
        receiverNumberText = findViewById(R.id.receiverNumberText)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            val receiver: Receiver = Receiver(receiverNameText.text.toString(), receiverNumberText.text.toString())
            val amount: String = amountText.text.toString()


            disposable = paymentServe.sendPayment(NewPayment(receiver, amount.toDouble()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { _ ->  navigateMainActivity()},
                            { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                    )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent: Intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, 0)
        return true
    }

    fun navigateMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }
}
