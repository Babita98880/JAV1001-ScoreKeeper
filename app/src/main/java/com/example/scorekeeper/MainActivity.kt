package com.example.scorekeeper
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import android.content.SharedPreferences
import com.example.scorekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private var team1Score = 0
    private var team2Score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        // Retrieve night mode preference from SharedPreferences
        sharedPreferences = getSharedPreferences("scorekeeper", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(nightMode)


        super.onCreate(savedInstanceState)
        // Initialize binding for the activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Check if the save score setting is true, if not, set default team scores to 0
        val saveScoreSetting = sharedPreferences.getBoolean("SAVE_SCORES", false)
        team1Score = if (saveScoreSetting) sharedPreferences.getInt("TEAM_A_SCORE", 0) else 0
        team2Score = if (saveScoreSetting) sharedPreferences.getInt("TEAM_B_SCORE", 0) else 0

        // Update the UI with the current scores
        updateTeam1ScoreText()
        updateTeam2ScoreText()

        // Set up the scoring options in the spinner
        val scoringOptions = listOf("1", "2", "3", "6")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, scoringOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.scoringSpinnerTeam1.adapter = adapter
        binding.scoringSpinnerTeam2.adapter = adapter

//        // Retrieve scores from SharedPreferences and update the UI
//        team1Score = sharedPreferences.getInt("team1_score", 0)
//        team2Score = sharedPreferences.getInt("team2_score", 0)
//        updateTeam1ScoreText()
//        updateTeam2ScoreText()

        // Handle the click events for the buttons
        binding.team1IncreaseButton.setOnClickListener {
            val score = binding.scoringSpinnerTeam1.selectedItem.toString().toInt()
            increaseTeam1Score(score)
        }

        binding.team1DecreaseButton.setOnClickListener {
            val score = binding.scoringSpinnerTeam1.selectedItem.toString().toInt()
            decreaseTeam1Score(score)
        }

        binding.team2IncreaseButton.setOnClickListener {
            val score = binding.scoringSpinnerTeam2.selectedItem.toString().toInt()
            increaseTeam2Score(score)
        }

        binding.team2DecreaseButton.setOnClickListener {
            val score = binding.scoringSpinnerTeam2.selectedItem.toString().toInt()
            decreaseTeam2Score(score)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu layout
        menuInflater.inflate(R.menu.menu_main, menu)
        // Find the night mode toggle switch in the menu
        val nightModeItem = menu?.findItem(R.id.action_toggle_daynight_mode)
        val nightModeSwitch = nightModeItem?.actionView as? SwitchCompat
        // Set the switch state based on the current night mode
        nightModeSwitch?.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)

        // Add a listener to the night mode switch to update the night mode setting
        nightModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            val newNightMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else
                AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(newNightMode)

            // Save the new night mode setting and team scores to SharedPreferences
            val sharedPreferences = getSharedPreferences("scorekeeper", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putInt("night_mode", newNightMode)
                putInt("team1_score", team1Score) // Save Team 1 score
                putInt("team2_score", team2Score) // Save Team 2 score
                apply()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    // Increase Team 1 score by the given score value
    private fun increaseTeam1Score(score: Int) {
        team1Score += score
        updateTeam1ScoreText()
    }

    // Handle decreasing the Team 1 score by the given score value, ensuring it doesn't go below zero
    private fun decreaseTeam1Score(score: Int) {
        if (team1Score >= score) {
            team1Score -= score
            updateTeam1ScoreText()
        } else {
            // If the score goes below zero, set it to zero and show a message to the user
            team1Score = 0
            showToast("Team A score cannot go below zero!")
        }
    }

    // Increase Team 2 score by the given score value
    private fun increaseTeam2Score(score: Int) {
        team2Score += score
        updateTeam2ScoreText()
    }

    // Handle decreasing the Team 2 score by the given score value, ensuring it doesn't go below zero
    private fun decreaseTeam2Score(score: Int) {
        if (team2Score >= score) {
            team2Score -= score
            updateTeam2ScoreText()
        } else {
            // If the score goes below zero, set it to zero and show a message to the user
            team2Score = 0
            showToast("Team B score cannot go below zero!")
        }
    }

    // Show a short toast message on the screen
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Update the displayed Team 1 score on the UI
    private fun updateTeam1ScoreText() {
        binding.team1Score.text = team1Score.toString()
        saveScores()
    }

    // Update the displayed Team 2 score on the UI
    private fun updateTeam2ScoreText() {
        binding.team2Score.text = team2Score.toString()
        saveScores()
    }


    // Menu item selection handling
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                showAbout()
                true
            }
            R.id.menu_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAbout() {
        val developerInfo = "Developer: Babita Rawat\nCourse Code: A00280963"
        Toast.makeText(this, developerInfo, Toast.LENGTH_LONG).show()
    }

    private fun openSettings() {
        // Implement this method to open the Settings Activity.
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun saveScores() {
        sharedPreferences.edit()
            .putInt("TEAM_A_SCORE", team1Score)
            .putInt("TEAM_B_SCORE", team2Score)
            .apply()
    }

    override fun onPause() {
        super.onPause()
        saveScores()
    }

    companion object {
        private const val REQUEST_SETTINGS = 1
    }
}
