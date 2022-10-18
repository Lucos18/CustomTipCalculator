package com.example.tiptime

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.*

lateinit var binding: ActivityMainBinding

/*
Revision of the code:
-Storage of service cost values and number of people when changing pages;
-Correction calculations for tip, sum and price of service per person and specified strings with its values
* */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_Homepage)
        //
        if (intent.getDoubleExtra("cost", 0.0) != 0.0) {
            binding.costOfService.editText?.setText(
                intent.getDoubleExtra("cost", 0.0).toString()
            )
            binding.numberOfPeople.editText?.setText(
                intent.getIntExtra("people", 0).toString()
            )
        }
        setSupportActionBar(toolbar)
        binding.calculateButton.setOnClickListener { calculateTip() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_homepage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.SettingsButton) navigateToSettingsActivity()
        return super.onOptionsItemSelected(item)
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
                var currency = intent.getStringExtra("selectedCurrency")

                //Get the current checked Radio Button to calculate the tips on selected percentage
                val tipPercentage = returnTipPercentage(binding.tipOptions.checkedRadioButtonId)

                //Calculate the values of the tips and services
                var tiptotal = tipPercentage * cost
                var tipForPerson = tiptotal / numberOfPeople
                if (binding.roundUpSwitch.isChecked) {
                    tipForPerson = roundUpDouble(tipForPerson)
                }
                var totalBillToPayPerPerson = (cost / numberOfPeople) + tipForPerson
                var totalBillToPay = totalBillToPayPerPerson * numberOfPeople
                var servicePerPerson = cost / numberOfPeople

                //If currency in the settings is different from null, then convert every value to the selected currency
                //TODO Change method to a cleaner one
                if (currency != null) {
                    currencyToFormat = transformCurrencySymbolToLocale(currency)
                    totalBillToPay = changeCurrencyToSelected(totalBillToPay, currency)
                    totalBillToPayPerPerson = totalBillToPay / numberOfPeople
                    tipForPerson = totalBillToPayPerPerson - servicePerPerson

                    if (binding.roundUpSwitch.isChecked) {
                        totalBillToPayPerPerson =
                            roundUpDouble(totalBillToPayPerPerson)
                        tipForPerson = totalBillToPayPerPerson - servicePerPerson
                        totalBillToPay = totalBillToPayPerPerson * numberOfPeople
                    }
                }
                //Format total values of the bills in current currency
                val formattedTotalBillPerPerson =
                    formatDoubleToCurrency(totalBillToPayPerPerson, currencyToFormat)
                val totalBill = formatDoubleToCurrency(totalBillToPay, currencyToFormat)
                val formattedTipForPerson = formatDoubleToCurrency(tipForPerson, currencyToFormat)
                val formattedServicePerPerson =
                    formatDoubleToCurrency(servicePerPerson, currencyToFormat)

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

    /**
     * It will accept a currency as a string and it will return it to Locale location
     */
    private fun transformCurrencySymbolToLocale(currency: String): Locale {
        return when (currency) {
            "$" -> Locale.US
            "€" -> Locale.ITALY
            else -> Locale.getDefault()
        }
    }

    /**
     * Accepts a checkedRadioButton and it will return a double to calculate the percentage of the
     * tip
     */
    private fun returnTipPercentage(checkedRadioButton: Int): Double {
        return when (checkedRadioButton) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            R.id.option_fifteen_percent -> 0.15
            else -> 0.00
        }
    }

    private fun changeCurrencyToSelected(doubleValue: Double, currencySettings: String): Double {
        var doubleValueConverted = doubleValue
        val currencyLocale: Currency = Currency.getInstance(Locale.getDefault())
        when (currencyLocale.symbol) {
            "$" -> if (currencySettings == "€") {
                doubleValueConverted *= 1.03
            }
            "€" -> if (currencySettings == "$") {
                doubleValueConverted /= 0.98
            }
        }
        return doubleValueConverted
    }

    /**
     *  Show a Toast message on the bottom of the device
     */
    private fun showToastErrorMessage(errorMessageId: Int) {
        Toast.makeText(applicationContext, getString(errorMessageId), Toast.LENGTH_SHORT).show()
    }

    /**
     * Will format any Double passed as parameter to a Currency that you want to be converted
     * and it accepts Locale as Location currency
     */
    private fun formatDoubleToCurrency(doubleValue: Double, Locale: Locale): String {
        return NumberFormat.getCurrencyInstance(Locale).format(doubleValue)
    }

    /**
     * Will round up every Double passed as parameter to the highest number possible
     */
    private fun roundUpDouble(value: Double): Double {
        return kotlin.math.ceil(value)
    }

    /**
     * Used as a click listener that will change the round up switch to disabled if the tip selected
     * is 0%, else it will enabled to be clicked
     */
    fun onRadioButtonClicked(view: View) {
        if (returnTipPercentage(binding.tipOptions.checkedRadioButtonId) == 0.00) {
            binding.roundUpSwitch.isClickable = false
            binding.roundUpSwitch.isChecked = false
        } else binding.roundUpSwitch.isClickable = true
    }

    /**
     * It will navigate to the settings activity
     */
    private fun navigateToSettingsActivity() {
        val intent = Intent(this@MainActivity, SettingsScreenActivity::class.java)
        intent.putExtra("cost", binding.costOfServiceEditText.text.toString().toDoubleOrNull())
        intent.putExtra("people", binding.numberOfPeopleEditText.text.toString().toIntOrNull())
        startActivity(intent)
    }
}