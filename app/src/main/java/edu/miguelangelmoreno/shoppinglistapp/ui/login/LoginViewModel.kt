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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthRepositoryImp: FirebaseAuthRepositoryImp
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState

    fun validateLogin(email: String, password: String) {
        _loginState.value = LoginState(
            emailIsValid = validateEmail(email),
            passwordIsValid = validatePassword(password)
        )

    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val success = firebaseAuthRepositoryImp.login(email, password)
            _loginState.value = LoginState(
                isLoading = true,
                isSuccessful = success
            )
        }
    }


}
