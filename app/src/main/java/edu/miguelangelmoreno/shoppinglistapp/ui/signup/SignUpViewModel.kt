package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.data.repository.UserRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginResponse
import edu.miguelangelmoreno.shoppinglistapp.utils.validateEmail
import edu.miguelangelmoreno.shoppinglistapp.utils.validateLastName
import edu.miguelangelmoreno.shoppinglistapp.utils.validateName
import edu.miguelangelmoreno.shoppinglistapp.utils.validatePassword
import edu.miguelangelmoreno.shoppinglistapp.utils.validateRepeatPassword
import edu.miguelangelmoreno.shoppinglistapp.utils.validatePhone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState>
        get() = _signUpState


    fun signUp(user: User) {
        viewModelScope.launch {
            _signUpState.value = SignUpState(isLoading = true)
            val response = userRepository.createUser(user)
            if(response.isSuccessful){
                _signUpState.value = SignUpState(isSuccessful = response.isSuccessful, isLoading = false)
            }else{
                val errorMessage = when (response.code()) {
                    HttpURLConnection.HTTP_NOT_ACCEPTABLE -> R.string.http_not_acceptable
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> R.string.http_internal_error
                    403 -> R.string.http_forbidden
                    else -> R.string.http_default_error
                }
                _signUpState.value = SignUpState(isLoading = false, isSuccessful = false, signUpError = errorMessage)
            }
            resetState()
        }
    }
    private fun resetState() {
        _signUpState.value = SignUpState()
    }
    fun isNameValid(context: Context, name: String) {
        val nameResponse = validateName(context, name)

        val nameErrorMessage = when (nameResponse) {
            is SignUpResponse.NameError -> nameResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            nameIsValid = (nameResponse == SignUpResponse.Success),
            nameErrorMessage = nameErrorMessage
        )
    }
    fun isLastNameValid(context: Context, lastName: String) {
        val lastNameResponse = validateLastName(context, lastName)

        val lastNameErrorMessage = when (lastNameResponse) {
            is SignUpResponse.LastNameError -> lastNameResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            lastNameIsValid = (lastNameResponse == SignUpResponse.Success),
            lastNameErrorMessage = lastNameErrorMessage
        )
    }
    fun isEmailValid(context: Context, email: String) {
        val emailResponse = validateEmail(context, email)

        val emailErrorMessage = when (emailResponse) {
            is LoginResponse.EmailError -> emailResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            emailIsValid = (emailResponse == LoginResponse.Success),
            emailErrorMessage = emailErrorMessage
        )
    }

    fun isPhoneValid(context: Context, phone: String) {
        val phoneResponse = validatePhone(context, phone)

        val phoneErrorMessage = when (phoneResponse) {
            is SignUpResponse.PhoneError -> phoneResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            phoneIsValid = (phoneResponse == SignUpResponse.Success),
            phoneErrorMessage = phoneErrorMessage
        )
    }

    fun isPasswordValid(context: Context, password: String) {
        val passwordResponse = validatePassword(context, password)

        val passwordErrorMessage = when (passwordResponse) {
            is LoginResponse.PasswordError -> passwordResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            passwordIsValid = (passwordResponse == LoginResponse.Success),
            passwordErrorMessage = passwordErrorMessage
        )
    }
    fun isRepeatPasswordValid(context: Context, password: String, repeatPassword: String) {
        val repeatPasswordResponse = validateRepeatPassword(context, password, repeatPassword)

        val repeatPasswordErrorMessage = when (repeatPasswordResponse) {
            is SignUpResponse.RepeatPasswordError -> repeatPasswordResponse.message
            else -> null
        }

        _signUpState.value = _signUpState.value.copy(
            passwordRepeatedIsValid = (repeatPasswordResponse == SignUpResponse.Success),
            repeatPasswordErrorMessage = repeatPasswordErrorMessage
        )
    }
}