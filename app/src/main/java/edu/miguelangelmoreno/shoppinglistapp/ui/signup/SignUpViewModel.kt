package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.core.ex.*
import edu.miguelangelmoreno.shoppinglistapp.data.remote.FirebaseAuthRepositoryImp
import edu.miguelangelmoreno.shoppinglistapp.data.remote.FirebaseFirestoreRepositoryImp
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseFirestoreResponse
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firestoreRepositoryImp: FirebaseFirestoreRepositoryImp,
    private val authRepositoryImp: FirebaseAuthRepositoryImp
) : ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState>
        get() = _signUpState

    fun validateSignUp(
        context: Context,
        name: String,
        lastName: String,
        phone: String,
        email: String,
        password: String,
        passwordRepeated: String
    ) {
        val nameResponse = validateName(context, name)
        val lastNameResponse = validateLastName(context, lastName)
        val phoneResponse = validatePhone(context, phone)
        val emailResponse = validateEmail(context, email)
        val passwordResponse = validatePassword(context, password)
        val repeatPasswordResponse = validatePasswordRepeated(context, password, passwordRepeated)

        updateSignUpState(nameResponse, lastNameResponse, phoneResponse, emailResponse,
            passwordResponse, repeatPasswordResponse)
    }

    private fun updateSignUpState(
        nameResponse: SignUpResponse,
        lastNameResponse: SignUpResponse,
        phoneResponse: SignUpResponse,
        emailResponse: LoginResponse,
        passwordResponse: LoginResponse,
        repeatPasswordResponse: SignUpResponse
    ) {
        val nameErrorMessage = getErrorMessage(nameResponse)
        val lastNameErrorMessage = getErrorMessage(lastNameResponse)
        val phoneErrorMessage = getErrorMessage(phoneResponse)
        val emailErrorMessage = getErrorMessage(emailResponse)
        val passwordErrorMessage = getErrorMessage(passwordResponse)
        val repeatPasswordErrorMessage = getErrorMessage(repeatPasswordResponse)

        _signUpState.value = SignUpState(
            nameIsValid = nameResponse == SignUpResponse.Success,
            lastNameIsValid = lastNameResponse == SignUpResponse.Success,
            phoneIsValid = phoneResponse == SignUpResponse.Success,
            emailIsValid = emailResponse == LoginResponse.Success,
            passwordIsValid = passwordResponse == LoginResponse.Success,
            passwordRepeatedIsValid = repeatPasswordResponse == SignUpResponse.Success,
            nameErrorMessage = nameErrorMessage,
            lastNameErrorMessage = lastNameErrorMessage,
            phoneErrorMessage = phoneErrorMessage,
            emailErrorMessage = emailErrorMessage,
            passwordErrorMessage = passwordErrorMessage,
            repeatPasswordErrorMessage = repeatPasswordErrorMessage
        )
    }

    fun signUp(user: User) {
        viewModelScope.launch {
            var isSuccessful: Boolean
            val authResponse = authRepositoryImp.signUp(user.email, user.password)
            var errorMessage: String? = when (authResponse) {
                FirebaseAuthResponse.EmailAlreadyExists -> "El correo electrónico ya está en uso."
                is FirebaseAuthResponse.Error -> "Error desconocido: ${authResponse.message}"
                else -> null
            }
            isSuccessful = (authResponse == FirebaseAuthResponse.Success)

            if (isSuccessful) {
                val firestoreResponse = firestoreRepositoryImp.insertUser(user)
                errorMessage = when (firestoreResponse) {
                    is FirebaseFirestoreResponse.Error -> "Error en el registro del usuario"
                    else -> null
                }
                isSuccessful = (firestoreResponse == FirebaseFirestoreResponse.Success)
            }

            _signUpState.value = SignUpState(
                isLoading = true, isSuccessful = isSuccessful, signUpError = errorMessage
            )
        }
    }

    private fun getErrorMessage(response: SignUpResponse): String? {
        return when (response) {
            is SignUpResponse.NameError -> response.message
            is SignUpResponse.LastNameError -> response.message
            is SignUpResponse.PhoneError -> response.message
            is SignUpResponse.RepeatPasswordError -> response.message
            else -> null
        }
    }


    private fun getErrorMessage(response: LoginResponse): String? {
        return when (response) {
            is LoginResponse.EmailError -> response.message
            is LoginResponse.PasswordError -> response.message
            else -> null
        }
    }
}