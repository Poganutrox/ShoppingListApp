package edu.miguelangelmoreno.shoppinglistapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.prefs
import edu.miguelangelmoreno.shoppinglistapp.core.ex.dismissKeyboard
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivityLoginBinding
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
import edu.miguelangelmoreno.shoppinglistapp.ui.signup.SignUpActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        observeLoginState()

    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { loginState ->
                    with(binding) {
                        tilEmail.error = loginState.emailErrorMessage
                        tilPassword.error = loginState.passwordErrorMessage
                        progressBar.isVisible = loginState.isLoading
                    }

                    if (loginState.isSuccessful) {
                        HomeActivity.navigate(this@LoginActivity)
                    } else if (!loginState.loginErrorMessage.isNullOrEmpty()) {
                        Toast.makeText(
                            this@LoginActivity, loginState.loginErrorMessage, Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun initListeners() {
        with(binding) {
            cbRemember.setOnCheckedChangeListener { _, isChecked -> prefs.setRemember(isChecked) }
            btnLogin.setOnClickListener { onLoginClick(it) }
            tvSignUp.setOnClickListener {
                SignUpActivity.navigate(this@LoginActivity)
            }
        }
    }

    private fun onLoginClick(view: View) {
        view.dismissKeyboard()
        with(binding){
            val email = tieEmail.text.toString()
            val password = tiePassword.text.toString()

            validateFields(email, password)

            viewModel.loginState.value.let { loginState ->
                if (loginState.emailIsValid && loginState.passwordIsValid) {
                    progressBar.isVisible = true
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun validateFields(email: String, password: String) {
        viewModel.isEmailValid(this@LoginActivity, email)
        viewModel.isPasswordValid(this@LoginActivity, password)
    }
}