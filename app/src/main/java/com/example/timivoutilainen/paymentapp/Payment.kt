package com.example.timivoutilainen.paymentapp

data class PaymentModel(
        val id: Int,
        val transaction_type: Int,
        val transaction_date: String,
        var amount: Double,
        val receiver: String,
        val sender: String,
        var name: String = ""
        )

data class NewPayment(val receiver: Receiver, val amount: Double)

data class Receiver(val name: String, val phone_number: String)

data class PersonModel(val phone_number: String, val name: String)