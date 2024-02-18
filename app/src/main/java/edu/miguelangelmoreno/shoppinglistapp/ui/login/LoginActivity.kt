package edu.miguelangelmoreno.shoppinglistapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.core.ex.dismissKeyboard
import edu.miguelangelmoreno.shoppinglistapp.core.ex.loseFocusAfterAction
import edu.miguelangelmoreno.shoppinglistapp.core.ex.onTextChanged
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivityLoginBinding
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
import edu.miguelangelmoreno.shoppinglistapp.ui.signup.SignUpActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
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
                    }

                    if (loginState.isLoading) {
                        if (loginState.isSuccessful) {
                            HomeActivity.navigate(this@LoginActivity)
                        } else {
                            Toast.makeText(
                                this@LoginActivity, loginState.loginErrorMessage, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }


    private fun initListeners() {
        with(binding) {
            tieEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieEmail.onTextChanged { onFieldChanged() }

            tiePassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tiePassword.onTextChanged { onFieldChanged() }

            btnLogin.setOnClickListener {
                it.dismissKeyboard()
                viewModel.loginState.value.let { loginState ->
                    val email = tieEmail.text.toString()
                    val password = tiePassword.text.toString()

                    if (loginState.emailIsValid && loginState.passwordIsValid) {
                        viewModel.login(email, password)
                    } else {
                        viewModel.validateLogin(this@LoginActivity, email, password)
                    }
                }
            }

            btnSignUp.setOnClickListener {
                SignUpActivity.navigate(this@LoginActivity)
            }
        }
    }

    private fun onFieldChanged() {
        with(binding) {
            val email = tieEmail.text.toString()
            val password = tiePassword.text.toString()

            viewModel.validateLogin(this@LoginActivity, email, password)
        }
    }


}