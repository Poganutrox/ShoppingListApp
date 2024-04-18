package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
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
import edu.miguelangelmoreno.shoppinglistapp.ui.home.HomeActivity
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
                        tilName.error = signUpState.nameErrorMessage
                        tilLastName.error = signUpState.lastNameErrorMessage
                        tilEmail.error = signUpState.emailErrorMessage
                        tilPhone.error = signUpState.phoneErrorMessage
                        tilPassword.error = signUpState.passwordErrorMessage
                        tilRepeatPassword.error = signUpState.repeatPasswordErrorMessage
                        //progressBar.isVisible = signUpState.isLoading
                    }

                    if (signUpState.isSuccessful) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Cuenta creada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else if (!signUpState.signUpError.isNullOrEmpty()) {
                        Toast.makeText(
                            this@SignUpActivity, signUpState.signUpError, Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            tieName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }
            tieLastName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }
            tiePhone.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }
            tieEmail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }
            tiePassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }
            tieRepeatPassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) onFocusLost()
            }

            tieName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieLastName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieRepeatPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)

            btnCreateAccount.setOnClickListener { onSignUpClick(it) }
        }
    }

    private fun onSignUpClick(view: View) {
        view.dismissKeyboard()
        viewModel.signUpState.value.let { signUpState ->
            with(binding) {
                val name = tieName.text.toString()
                val lastName = tieLastName.text.toString()
                val email = tieEmail.text.toString()
                val phone = tiePhone.text.toString()
                val password = tiePassword.text.toString()
                val repeatPassword = tieRepeatPassword.text.toString()

                if (signUpState.nameIsValid && signUpState.lastNameIsValid && signUpState.emailIsValid
                    && signUpState.passwordIsValid && signUpState.passwordRepeatedIsValid
                    && signUpState.phoneIsValid
                ) {
                    //progressBar.isVisible = true
                    val newUser = User(null, name, lastName, phone, email, password)
                    viewModel.signUp(newUser)
                } else {
                    viewModel.isNameValid(this@SignUpActivity, name)
                    viewModel.isLastNameValid(this@SignUpActivity, lastName)
                    viewModel.isEmailValid(this@SignUpActivity, email)
                    viewModel.isPhoneValid(this@SignUpActivity, phone)
                    viewModel.isPasswordValid(this@SignUpActivity, password)
                    viewModel.isRepeatPasswordValid(this@SignUpActivity, password, repeatPassword)
                }
            }


        }
    }

    private fun onFocusLost() {
        with(binding) {
            val name = tieName.text.toString()
            val lastName = tieLastName.text.toString()
            val email = tieEmail.text.toString()
            val phone = tiePhone.text.toString()
            val password = tiePassword.text.toString()
            val repeatPassword = tieRepeatPassword.text.toString()

            if (!tieName.hasFocus()) {
                viewModel.isNameValid(this@SignUpActivity, name)
            }

            if (!tieLastName.hasFocus()) {
                viewModel.isLastNameValid(this@SignUpActivity, lastName)
            }

            if (!tieEmail.hasFocus()) {
                viewModel.isEmailValid(this@SignUpActivity, email)
            }

            if (!tiePhone.hasFocus()) {
                viewModel.isPhoneValid(this@SignUpActivity, phone)
            }


            if (!tiePassword.hasFocus()) {
                viewModel.isPasswordValid(this@SignUpActivity, password)
            }

            if (!tieRepeatPassword.hasFocus()) {
                viewModel.isRepeatPasswordValid(this@SignUpActivity, password, repeatPassword)
            }
        }
    }

}