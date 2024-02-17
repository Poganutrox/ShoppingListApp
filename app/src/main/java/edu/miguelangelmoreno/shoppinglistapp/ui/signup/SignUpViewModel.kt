package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.core.ex.*
import edu.miguelangelmoreno.shoppinglistapp.data.remote.FirebaseFirestoreRepositoryImp
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firestoreRepositoryImp: FirebaseFirestoreRepositoryImp
): ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState>
        get() = _signUpState

    fun validateSignUp(name : String, lastName : String, phone : String, email: String, password: String, passwordRepeated: String) {
        _signUpState.value = SignUpState(
            nameIsValid = validateName(name),
            lastNameIsValid = validationLastName(lastName),
            phoneIsValid = validatePhone(phone) ,
            emailIsValid = validateEmail(email),
            passwordIsValid = validatePassword(password),
            passwordRepeatedIsValid = validatePasswordRepeated(password, passwordRepeated)
        )

    }

    fun signUp(user : User) {
        viewModelScope.launch {
            val success = firestoreRepositoryImp.insertUser(user)
            _signUpState.value = SignUpState(
                isLoading = true,
                isSuccessful = success
            )
        }
    }
}