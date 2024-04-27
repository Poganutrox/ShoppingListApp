package edu.miguelangelmoreno.shoppinglistapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.repository.UserRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.utils.validateEmail
import edu.miguelangelmoreno.shoppinglistapp.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            val response = userRepository.checkAccess(
                User(email = email, password = password)
            )

            var loginErrorMessage : Int? = null
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    userPrefs.saveUser(user)
                }
            } else {
                loginErrorMessage = when (response.code()) {
                    HttpURLConnection.HTTP_NOT_ACCEPTABLE -> R.string.http_invalid_credentials
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> R.string.http_internal_error
                    403 -> R.string.http_forbidden
                    else -> R.string.http_default_error
                }
            }
            _loginState.value = _loginState.value.copy(
                isLoading = false,
                isSuccessful = response.isSuccessful,
                loginErrorMessage = loginErrorMessage
            )

            resetState()
        }
    }

    private fun resetState() {
        _loginState.value = LoginState()
    }
    fun isEmailValid(context: Context, email: String) {
        val emailResponse = validateEmail(context, email)

        val emailErrorMessage = when (emailResponse) {
            is LoginResponse.EmailError -> emailResponse.message
            else -> null
        }

        _loginState.value = _loginState.value.copy(
            emailIsValid = (emailResponse == LoginResponse.Success),
            emailErrorMessage = emailErrorMessage
        )
    }

    fun isPasswordValid(context: Context, password: String) {
        val passwordResponse = validatePassword(context, password)

        val passwordErrorMessage = when (passwordResponse) {
            is LoginResponse.PasswordError -> passwordResponse.message
            else -> null
        }

        _loginState.value = _loginState.value.copy(
            passwordIsValid = (passwordResponse == LoginResponse.Success),
            passwordErrorMessage = passwordErrorMessage
        )
    }
}
