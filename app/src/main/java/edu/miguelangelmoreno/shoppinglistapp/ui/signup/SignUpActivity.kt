package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivitySignUpBinding
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        observeSignUpState()
    }

    private fun observeSignUpState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUpState.collect { signUpState ->
                    if(signUpState.isLoading){
                        if (signUpState.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "Usuario registrado", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Error al registrar usuario", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            tieName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieName.onTextChanged { onFieldChanged() }

            tieLastName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieLastName.onTextChanged { onFieldChanged() }


            tieEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieEmail.onTextChanged { onFieldChanged() }

            tiePhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePhone.onTextChanged { onFieldChanged() }

            tiePassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePassword.onTextChanged { onFieldChanged() }

            tieRepeatPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            tieRepeatPassword.onTextChanged { onFieldChanged() }

            btnCreateAccount.setOnClickListener {
                it.dismissKeyboard()
                viewModel.signUpState.value.let { signUpState ->
                    if (signUpState.nameIsValid && signUpState.lastNameIsValid && signUpState.emailIsValid
                        && signUpState.phoneIsValid && signUpState.passwordIsValid && signUpState.passwordRepeatedIsValid
                    ) {
                        val newUser = User(
                            name = tieName.text.toString(),
                            lastName = tieLastName.text.toString(),
                            email = tieEmail.text.toString(),
                            phone = tiePhone.text.toString()
                        )

                        viewModel.signUp(newUser)
                    }
                }
            }
        }
    }

    private fun onFieldChanged() {
        with(binding) {
            val name = tieName.text.toString()
            val lastName = tieLastName.text.toString()
            val email = tieEmail.text.toString()
            val phone = tiePhone.text.toString()
            val password = tiePassword.text.toString()
            val repeatPassword = tieRepeatPassword.text.toString()

            viewModel.validateSignUp(name, lastName, phone, email, password, repeatPassword)

            viewModel.signUpState.value.let { loginState ->
                if (!loginState.emailIsValid) {
                    tilEmail.error = getString(R.string.login_error_email)
                } else {
                    tilEmail.error = null
                }

                if (!loginState.passwordIsValid) {
                    tilPassword.error = getString(R.string.login_error_password)
                } else {
                    tilPassword.error = null
                }
            }
        }
    }

}