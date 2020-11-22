package dev.keader.correiostracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import dev.keader.correiostracker.databinding.ActivityMainBinding
import dev.keader.correiostracker.view.home.HomeFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val _uiViewModel: UIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpNavigation()

        binding.floatingActionButton.setOnClickListener { view ->
            view.findNavController()
                .navigate(HomeFragmentDirections.actionGlobalAddPacketFragment())
        }

        _uiViewModel.bottomNavVisibility.observe(this, { visibility ->
            binding.bottomNavigation.visibility = visibility
            binding.floatingActionButton.visibility = visibility
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val code = result.contents
                _uiViewModel.setQrCode(code)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getSnackInstance(string: String, duration: Int): Snackbar? {
        return Snackbar.make(binding.root, string, duration)
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)
        Navigation.setViewNavController(binding.root, navHostFragment.navController)
    }
}
