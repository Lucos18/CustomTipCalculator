package com.example.tiptime.data.local

import com.example.tiptime.R

object LocalCurrencyData {

    val cu = listOf<String>(
        "EUR",
        "US"
    )
}
enum class CurrencyData(
    val Code: String,
    val Flag: Int,
    val rate: Double
){
    EUR("EUR", R.drawable.it_flag, 1.00),
    USD("USD", R.drawable.us_flag, 1.00),
    GBP("GBP", R.drawable.uk_flag, 1.00)
}