package com.example.tiptime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import com.example.tiptime.databinding.ActivitySettingsBinding
import java.text.NumberFormat
import java.util.*

lateinit var bindingActivity: ActivitySettingsBinding

class SettingsScreenActivity : AppCompatActivity() {
    private var selectedCurrency: String = "$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)
        supportActionBar?.title = "Settings Menu"
        bindingActivity.arrowBackMainActivity.setOnClickListener{ navigateToMainActivity() }
    }
    private fun navigateToMainActivity(){
        val intent = Intent(this@SettingsScreenActivity, MainActivity::class.java)
        intent.putExtra("selectedCurrency", selectedCurrency)
        startActivity(intent)
    }
    fun onRadioButtonSettingsClicked(view: View) {
        when (bindingActivity.currencyOptions.checkedRadioButtonId){
            R.id.dollar_option -> selectedCurrency = "$"
            R.id.euro_option -> selectedCurrency = "€"
        }
    }
}