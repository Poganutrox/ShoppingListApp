package edu.miguelangelmoreno.shoppinglistapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.remote.FirebaseAuthRepositoryImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import edu.miguelangelmoreno.shoppinglistapp.core.ex.validateEmail
import edu.miguelangelmoreno.shoppinglistapp.core.ex.validatePassword
import edu.miguelangelmoreno.shoppinglistapp.data.response.FirebaseAuthResponse

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState

    fun validateLogin(email: String, password: String) {
        val emailResponse = validateEmail(email)
        val passwordResponse = validatePassword(password)

        val emailErrorMessage = when (emailResponse) {
            is LoginResponse.EmailError -> emailResponse.message
            else -> null
        }
        val passwordErrorMessage = when (passwordResponse) {
            is LoginResponse.PasswordError -> passwordResponse.message
            else -> null
        }

        _loginState.value = LoginState(
            emailIsValid = (emailResponse == LoginResponse.Success),
            passwordIsValid = (passwordResponse == LoginResponse.Success),
            emailErrorMessage = emailErrorMessage,
            passwordErrorMessage = passwordErrorMessage
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val firebaseResponse = firebaseAuthRepositoryImp.login(email, password)
            val loginErrorMessage = when(firebaseResponse){
                is FirebaseAuthResponse.Error -> firebaseResponse.message
                FirebaseAuthResponse.InvalidCredentials -> "Credenciales no válidas"
                FirebaseAuthResponse.UserNotExists -> "Usuario no registrado"
                else -> null
            }
            _loginState.value = LoginState(
                isLoading = true,
                isSuccessful = (firebaseResponse == FirebaseAuthResponse.Success),
                loginErrorMessage = loginErrorMessage
            )
        }
    }


}
