package com.example.timivoutilainen.paymentapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        amountText = findViewById(R.id.amountText)
        receiverNameText = findViewById(R.id.receiverNameText)
        receiverNumberText = findViewById(R.id.receiverNumberText)
        button = this.findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.INVISIBLE

        reset()

        button.setOnClickListener {
            val receiver: Receiver? = Receiver(receiverNameText.text.toString(), receiverNumberText.text.toString())
            val amount: String? = amountText.text.toString()

            loading()

            if (amount != null) {
                try {
                    var amountDouble = amount.toDouble()
                    disposable = paymentServe.sendPayment(NewPayment(receiver!!, amountDouble))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { _ ->  navigateMainActivity() },
                                    { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show(); reset() }
                            )
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    reset()
                }
            } else {
                reset()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent: Intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, 0)
        return true
    }

    private fun navigateMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }

    fun reset() {
        progressBar.visibility = View.INVISIBLE
        button.visibility = View.VISIBLE
    }

    fun loading() {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.INVISIBLE
    }
}
