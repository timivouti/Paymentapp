package com.example.timivoutilainen.paymentapp

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface PaymentService {
    @GET("transactions")
    fun getResult(): Observable<ArrayList<PaymentModel>>

    @GET("persons")
    fun getPersons(): Observable<ArrayList<PersonModel>>

    @POST("transactions")
    fun sendPayment(@Body newPayment: NewPayment): Observable<Response<Void>>

    companion object {
        fun create(): PaymentService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://peaceful-stream-34212.herokuapp.com/")
                    .build()

            return retrofit.create(PaymentService::class.java)
        }
    }
}