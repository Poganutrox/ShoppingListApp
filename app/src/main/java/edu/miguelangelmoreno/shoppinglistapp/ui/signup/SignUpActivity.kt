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
                    with(binding) {
                        tilEmail.error = signUpState.emailErrorMessage
                        tilPassword.error = signUpState.passwordErrorMessage
                        tilName.error = signUpState.nameErrorMessage
                        tilLastName.error = signUpState.lastNameErrorMessage
                        tilPhone.error = signUpState.phoneErrorMessage
                        tilRepeatPassword.error = signUpState.repeatPasswordErrorMessage
                    }
                    if (signUpState.isLoading) {
                        if (!signUpState.isSuccessful) {
                            Toast.makeText(
                                this@SignUpActivity,
                                signUpState.signUpError,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Cuenta creada con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
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
                            phone = tiePhone.text.toString(),
                            password = tiePassword.toString()
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

            viewModel.validateSignUp(this@SignUpActivity, name, lastName, phone, email, password, repeatPassword)
        }
    }

}