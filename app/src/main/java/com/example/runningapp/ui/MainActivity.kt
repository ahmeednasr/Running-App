package com.example.runningapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runningapp.Constants
import com.example.runningapp.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.runningapp.R
import com.example.runningapp.databinding.ActivityMainBinding
import com.example.runningapp.ui.fragments.SetupFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateToTrackingFragment(intent)
        setSupportActionBar(binding.toolbar)
        loadNamesFromSharedPref()

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { /**/ }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                        if (binding.bottomNavigationView.visibility != View.VISIBLE) {
                            binding.bottomNavigationView.visibility = View.VISIBLE
                        }

                    else -> {
                        if (binding.bottomNavigationView.visibility != View.GONE) {
                            binding.bottomNavigationView.visibility = View.GONE
                        }
                    }
                }
            }

    }

    private fun loadNamesFromSharedPref() {
        val nameText = sharedPreferences.getString(Constants.KEY_NAME, "")
        val toolbarText = "Let's go $nameText"
        this.findViewById<TextView>(R.id.tvToolbarTitle).text = toolbarText

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

    private fun navigateToTrackingFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            binding.navHostFragment.findNavController()
                .navigate(R.id.action_global_trackingFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}