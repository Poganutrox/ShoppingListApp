package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.data.repository.UserRepository
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _accountState = MutableStateFlow(AccountState())
    val accountState: StateFlow<AccountState>
        get() = _accountState

    fun updateUser(user: User) {
        viewModelScope.launch {
            _accountState.value = AccountState(isLoading = true)
            val response = userRepository.updateUser(user)
            if (response.isSuccessful) {
                _accountState.value = AccountState(isUpdated = true, isLoading = false)
            } else {
                val errorMessage = when (response.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> R.string.http_bad_request
                    HttpURLConnection.HTTP_NOT_FOUND -> R.string.http_not_found
                    HttpURLConnection.HTTP_NOT_ACCEPTABLE -> R.string.http_not_acceptable
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> R.string.http_internal_error
                    403 -> R.string.http_forbidden
                    else -> R.string.http_default_error
                }
                _accountState.value = AccountState(isUpdated = false, isLoading = false, errorMessage = errorMessage)
            }
            resetState()
        }
    }

    private fun resetState() {
        _accountState.value = AccountState()
    }
}