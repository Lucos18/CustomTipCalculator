package com.example.tiptime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.*

lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }
        binding.Settings.setOnClickListener{ navigateToSettingsActivity() }
    }

    private fun navigateToSettingsActivity(){
         val intent = Intent(this@MainActivity, SettingsScreenActivity::class.java)
         startActivity(intent)
    }
    private fun calculateTip() {

        //Get the user input text inside the EditableText, if empty returns null
        var cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()
        val numberOfPeople = binding.numberOfPeopleEditText.text.toString().toIntOrNull()

        //Check that the cost isn't null
        if (cost != null) {

            //Check that the cost isn't null and the number of people isn't 0
            if (numberOfPeople != null && numberOfPeople != 0) {
                var currencyToFormat = Locale.getDefault()
                var currency = intent.getStringExtra("selectedCurrency");

                //Get the current checked Radio Button to calculate the tips on selected percentage
                val tipPercentage = returnTipPercentage(binding.tipOptions.checkedRadioButtonId)

                //Calculate the values of the tips and services
                var tiptotal = tipPercentage * cost
                var tipForPerson = tiptotal / numberOfPeople
                if (binding.roundUpSwitch.isChecked)
                {
                    tiptotal = roundUpDouble(tiptotal)
                    tipForPerson = roundUpDouble(tipForPerson)
                }
                var totalBillToPayPerPerson = (cost / numberOfPeople) + tipForPerson
                var totalBillToPay = totalBillToPayPerPerson * numberOfPeople
                var servicePerPerson = cost / numberOfPeople
                if (currency != null) {
                    currencyToFormat = trasformCurrencySymbolToLocale(currency)
                    tiptotal = changeCurrencyToSelected(tiptotal, currency)
                    tipForPerson = changeCurrencyToSelected(tipForPerson, currency)
                    totalBillToPayPerPerson = changeCurrencyToSelected(totalBillToPayPerPerson, currency)
                    totalBillToPay = changeCurrencyToSelected(totalBillToPay, currency)
                    servicePerPerson = changeCurrencyToSelected(servicePerPerson, currency)
                }


                //Format total values of the bills in current currency

                val formattedTotalBillPerPerson = formatDoubleToCurrency(totalBillToPayPerPerson, currencyToFormat)
                val totalBill = formatDoubleToCurrency(totalBillToPay, currencyToFormat)
                val formattedTipForPerson = formatDoubleToCurrency(tipForPerson, currencyToFormat)
                val formattedServicePerPerson = formatDoubleToCurrency(servicePerPerson, currencyToFormat)

                //Place in the TextView the calculated prices in formatted currency string
                binding.totalBill.text = getString(R.string.total_bill, totalBill)
                binding.totalForPerson.text =
                    getString(R.string.total_for_person, formattedTotalBillPerPerson)
                binding.tipForPerson.text =
                    getString(R.string.tip_for_person, formattedTipForPerson)
                binding.servicePricePerPerson.text =
                    getString(R.string.service_price_per_person, formattedServicePerPerson)

            } else showToastErrorMessage(R.string.error_empty_number_of_people)
        } else showToastErrorMessage(R.string.error_empty_cost_of_service)

    }
    private fun trasformCurrencySymbolToLocale(currency: String):Locale{
        return when (currency){
            "$" -> Locale.CANADA
            "€" -> Locale.ITALY
            else -> Locale.getDefault()
        }
    }
    private fun returnTipPercentage(checkedRadioButton: Int): Double {
        return when (checkedRadioButton) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            R.id.option_fifteen_percent-> 0.15
            else -> 0.00
        }
    }

    private fun changeCurrencyToSelected(doubleValue: Double, currency: String): Double {
        var doubleValue2 = doubleValue
        val currencyLocale: Currency = Currency.getInstance(Locale.getDefault())
        Log.d("ciao",currency)
        when (currencyLocale.symbol){
            "$" -> if (currency == "€")
            {
                doubleValue2 *= 1.03
            }
            "€" -> if (currency == "$")
            {
                doubleValue2 /= 1.03
            }
            else -> doubleValue2 + 10000
        }
        return doubleValue2
    }

    private fun showToastErrorMessage(errorMessageId: Int) {
        Toast.makeText(applicationContext, getString(errorMessageId), Toast.LENGTH_SHORT).show()
    }

    private fun formatDoubleToCurrency(doubleValue: Double, Locale: Locale): String {
        return NumberFormat.getCurrencyInstance(Locale).format(doubleValue)
    }

    private fun roundUpDouble(value: Double): Double {
        return kotlin.math.ceil(value)
    }
    fun onRadioButtonClicked(view: View) {
        if (returnTipPercentage(binding.tipOptions.checkedRadioButtonId) == 0.00)
        {
            binding.roundUpSwitch.isClickable = false
            binding.roundUpSwitch.isChecked = false
        } else binding.roundUpSwitch.isClickable = true
    }
}