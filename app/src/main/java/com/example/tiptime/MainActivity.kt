package com.example.tiptime

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }

    }


    private fun calculateTip() {

        //Get the user input text inside the EditableText, if empty returns null
        var cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()
        val numberOfPeople = binding.numberOfPeopleEditText.text.toString().toIntOrNull()

        //Check that the cost isn't null
        if (cost != null) {

            //Check that the cost isn't null and the number of people isn't 0
            if (numberOfPeople != null && numberOfPeople != 0) {

                //Get the current checked Radio Button to calculate the tips on selected percentage
                val tipPercentage = returnTipPercentage(binding.tipOptions.checkedRadioButtonId)

                //Calculate the values of the tips and services
                val tiptotal =
                    if (binding.roundUpSwitch.isChecked) roundUpDouble(tipPercentage * cost) else tipPercentage * cost

                val tipForPerson =
                    if (binding.roundUpSwitch.isChecked) roundUpDouble(tiptotal / numberOfPeople) else tiptotal / numberOfPeople

                val totalBillToPayPerPerson = (cost / numberOfPeople) + tipForPerson
                val totalBillToPay = totalBillToPayPerPerson * numberOfPeople
                val servicePerPerson = cost / numberOfPeople

                //Format total values of the bills in current currency
                val formattedTotalBillPerPerson = formatDoubleToCurrency(totalBillToPayPerPerson)
                val totalBill = formatDoubleToCurrency(totalBillToPay)
                val formattedTipForPerson = formatDoubleToCurrency(tipForPerson)
                val formattedServicePerPerson = formatDoubleToCurrency(servicePerPerson)

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

    private fun returnTipPercentage(checkedRadioButton: Int): Double {
        return when (checkedRadioButton) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            R.id.option_fifteen_percent-> 0.15
            else -> 0.00
        }
    }

    private fun showToastErrorMessage(errorMessageId: Int) {
        Toast.makeText(applicationContext, getString(errorMessageId), Toast.LENGTH_SHORT).show()
    }

    private fun formatDoubleToCurrency(doubleValue: Double): String {
        return NumberFormat.getCurrencyInstance().format(doubleValue)
    }

    private fun roundUpDouble(value: Double): Double {
        return kotlin.math.ceil(value)
    }
}