package com.example.scorekeeper
// SettingsActivity.kt
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.scorekeeper.databinding.ActivitySettingsBinding




class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("scorekeeper", MODE_PRIVATE)

        // Load the current setting state from SharedPreferences
        val saveScores = sharedPreferences.getBoolean("SAVE_SCORES", false)
        binding.switchSaveScores.isChecked = saveScores

        // Set the listener for the Save Scores switch
        binding.switchSaveScores.setOnCheckedChangeListener { _, isChecked ->
            // Save the setting state to SharedPreferences
            sharedPreferences.edit()
                .putBoolean("SAVE_SCORES", isChecked)
                .apply()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // Go back to the previous activity
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
