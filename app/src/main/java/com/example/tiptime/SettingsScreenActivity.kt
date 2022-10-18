package com.example.tiptime

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tiptime.databinding.ActivitySettingsBinding
import java.util.*

lateinit var bindingActivity: ActivitySettingsBinding

class SettingsScreenActivity : AppCompatActivity() {
    private var selectedCurrency: String = "$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_Settings)
        toolbar.title = "Settings Page"
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener{navigateToMainActivity()}
    }
    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_settings_page, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.ArrowBackButton) navigateToMainActivity()
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SettingsScreenActivity, MainActivity::class.java)
        intent.putExtra("selectedCurrency", selectedCurrency)
        intent.putExtra("cost", binding.costOfServiceEditText.text.toString().toDoubleOrNull())
        intent.putExtra("people", binding.numberOfPeopleEditText.text.toString().toIntOrNull())
        startActivity(intent)
    }

    fun onRadioButtonSettingsClicked(view: View) {
        when (bindingActivity.currencyOptions.checkedRadioButtonId) {
            R.id.dollar_option -> selectedCurrency = "$"
            R.id.euro_option -> selectedCurrency = "€"
        }
    }
}