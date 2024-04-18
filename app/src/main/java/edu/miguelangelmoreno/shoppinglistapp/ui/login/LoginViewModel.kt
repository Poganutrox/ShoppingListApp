package edu.miguelangelmoreno.shoppinglistapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.utils.validateEmail
import edu.miguelangelmoreno.shoppinglistapp.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            val apiResponse =
                shoppingListRepository.checkAccess(User(email = email, password = password))

            _loginState.value = _loginState.value.copy(
                isLoading = false,
                isSuccessful = apiResponse.success,
                loginErrorMessage = apiResponse.error
            )
        }
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
