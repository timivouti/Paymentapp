package com.example.timivoutilainen.paymentapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private var payments: ArrayList<PaymentModel> = ArrayList()
    private lateinit var rv: RecyclerView
    private lateinit var adapter: CustomAdapter
    private val paymentServe by lazy { PaymentService.create() }
    private var disposable: Disposable? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        progressBar = findViewById(R.id.progressBar)

        getPayments()

        adapter = CustomAdapter(payments)
        rv.adapter = adapter
        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            val intent = Intent(this, AddPaymentActivity::class.java).apply{}
            startActivity(intent)
        }
    }

    private fun getPayments() {
        disposable = paymentServe.getResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> savePayments(result); getPersons() },
                        { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show(); removeProgressbar() }
                )
    }

    private fun savePayments(results: ArrayList<PaymentModel>) {
        results.forEach {
            result -> payments.add(result)
        }
    }

    private fun getPersons() {
        disposable = paymentServe.getPersons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> savePersons(result) },
                        { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show(); removeProgressbar() }
                )

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    /* OLD MAPPING
    private fun savePayments(results: ArrayList<PaymentModel>) {
        results.forEach {
            result ->
            var phone_number = if (result.sender == "0504567890") result.receiver else result.sender
            var year = result.transaction_date.substring(0, 4).toInt()
            var month = result.transaction_date.substring(5, 7).toInt()
            var day = result.transaction_date.substring(8, 10).toInt()
            payments.add(Payment(phone_number!!, result.amount, GregorianCalendar(year, month, day)))
        }
        getPersons()
    */

    private fun savePersons(result: ArrayList<PersonModel>) {
        payments.forEach {
            payment ->
            result.forEach {
                person ->
                if (payment.sender == "0504567890" && payment.receiver == person.phone_number) {
                    payment.name = person.name
                    payment.amount = - payment.amount
                } else if (payment.sender != "0504567890" && payment.sender == person.phone_number) {
                    payment.name = person.name
                }
            }
        }
        removeProgressbar()
        updateAdapter()
    }

    private fun removeProgressbar() {
        progressBar.visibility = View.GONE
    }

    private fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }


    /*
    //OLD GET REQUEST

    private inner class FetchPayments: AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            val url = URL("https://peaceful-stream-34212.herokuapp.com/transactions")
            val httpClient = url.openConnection() as HttpURLConnection
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    return readStream(inputStream = stream)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    httpClient.disconnect()
                }
            } else {
                Log.i("HTTP ERROR", "ERROR ${httpClient.responseCode}")
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var resultJSONArray: JSONArray = JSONArray(result)

            for (x in 0 until resultJSONArray.length()) {
                var name = resultJSONArray.getJSONObject(x).getString("receiver")
                var amount = resultJSONArray.getJSONObject(x).getString("amount").toDouble()
                var date = resultJSONArray.getJSONObject(x).getString("transaction_date")
                var year = date.substring(0, 4).toInt()
                var month = date.substring(5, 7).toInt()
                var day = date.substring(8, 10).toInt()
                payments.add(Payment(name, amount, GregorianCalendar(year, month, day)))
            }
            updateAdapter()
        }

        private fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
    }
    */
}
