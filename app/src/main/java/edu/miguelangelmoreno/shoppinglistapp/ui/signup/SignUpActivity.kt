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
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.core.ex.dismissKeyboard
import edu.miguelangelmoreno.shoppinglistapp.core.ex.loseFocusAfterAction
import edu.miguelangelmoreno.shoppinglistapp.databinding.ActivitySignUpBinding
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.utils.makeToast
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
    private lateinit var name: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var repeatPassword: String
    private lateinit var phone: String

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
                        collectStateErrors(signUpState)
                        progressBar.isVisible = signUpState.isLoading
                    }
                    val errorMessage: String? = signUpState.signUpError?.let { getString(it) }
                    if (signUpState.isSuccessful) {
                        makeToast(this@SignUpActivity, getString(R.string.signup_success))
                        finish()
                    } else if (!errorMessage.isNullOrEmpty()) {
                        makeToast(this@SignUpActivity, errorMessage)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            tieName.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilName)
            }
            tieLastName.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilLastName)
            }
            tiePhone.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilPhone)
            }
            tieEmail.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilEmail)
            }
            tiePassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilPassword)
            }
            tieRepeatPassword.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) clearError(tilRepeatPassword)
            }

            tieName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieLastName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tiePassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            tieRepeatPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)

            btnSignUp.setOnClickListener { onSignUpClick(it) }
        }
    }

    private fun onSignUpClick(view: View) {
        view.dismissKeyboard()

        collectFields()
        checkFields()

        viewModel.signUpState.value.let { signUpState ->
            if (signUpState.nameIsValid && signUpState.lastNameIsValid && signUpState.emailIsValid
                && signUpState.passwordIsValid && signUpState.passwordRepeatedIsValid
                && signUpState.phoneIsValid
            ) {
                val newUser = User(null, null, name, lastName, phone, email, password)
                viewModel.signUp(newUser)
            }
        }
    }


    private fun collectFields() {
        with(binding) {
            name = tieName.text.toString()
            lastName = tieLastName.text.toString()
            email = tieEmail.text.toString()
            phone = tiePhone.text.toString()
            password = tiePassword.text.toString()
            repeatPassword = tieRepeatPassword.text.toString()
        }
    }

    private fun checkFields() {
        with(viewModel) {
            isNameValid(this@SignUpActivity, name)
            isLastNameValid(this@SignUpActivity, lastName)
            isEmailValid(this@SignUpActivity, email)
            isPhoneValid(this@SignUpActivity, phone)
            isPasswordValid(this@SignUpActivity, password)
            isRepeatPasswordValid(this@SignUpActivity, password, repeatPassword)
        }
    }

    private fun clearError(til: TextInputLayout) {
        til.error = null
    }

    private fun collectStateErrors(signUpState: SignUpState) {
        with(binding) {
            tilName.error = signUpState.nameErrorMessage
            tilLastName.error = signUpState.lastNameErrorMessage
            tilEmail.error = signUpState.emailErrorMessage
            tilPhone.error = signUpState.phoneErrorMessage
            tilPassword.error = signUpState.passwordErrorMessage
            tilRepeatPassword.error = signUpState.repeatPasswordErrorMessage
        }
    }
}
