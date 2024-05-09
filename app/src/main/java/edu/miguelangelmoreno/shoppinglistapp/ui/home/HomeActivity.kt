package edu.miguelangelmoreno.shoppinglistapp.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

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
        initListeners()
        observeChanges()
    }
    private fun observeChanges(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                addPrefs.addingSize.collect{size ->
                    if(size > 0){
                        binding.linearTextImage.visibility = VISIBLE
                        binding.tvNumberItems.text = size.toString()
                    }else{
                        binding.linearTextImage.visibility = INVISIBLE
                    }
                }
            }
        }
    }

    private fun initUI(){
        val navController = this.findNavController(R.id.navHostFragment)
        binding.navBar.setupWithNavController(navController)
    }

    private fun initListeners(){
        val navController = this.findNavController(R.id.navHostFragment)
        binding.imgBasket.setOnClickListener {
            navController.popBackStack(R.id.addProductFragment, false)
        }
        binding.imgCancel.setOnClickListener {
            addPrefs.clear()
            addPrefs.setIsAdding(false)
            navController.popBackStack(R.id.homeFragment, false)
        }
    }
}