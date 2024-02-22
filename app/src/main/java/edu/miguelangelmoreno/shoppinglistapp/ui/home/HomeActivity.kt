package edu.miguelangelmoreno.shoppinglistapp.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()


    }

    private fun initUI(){
        val navController = this.findNavController(R.id.navHostFragment)
        binding.navBar.setupWithNavController(navController)
    }
}