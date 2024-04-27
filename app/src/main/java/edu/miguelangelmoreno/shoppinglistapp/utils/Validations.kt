package edu.miguelangelmoreno.shoppinglistapp.utils

import android.content.Context
import androidx.core.content.ContextCompat.getString
import edu.miguelangelmoreno.shoppinglistapp.R
import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginResponse
import edu.miguelangelmoreno.shoppinglistapp.ui.signup.SignUpResponse

private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
private val phoneRegex = Regex("^[6789]\\d{8}$")

fun validateRepeatPassword(
    context: Context,
    password: String,
    passwordRepeated: String
): SignUpResponse {
    if (passwordRepeated != password) {
        return SignUpResponse.RepeatPasswordError(context.getString(R.string.error_passwords_do_not_match))
    }
    return SignUpResponse.Success
}

fun validateName(context: Context, name: String): SignUpResponse {
    val nameConst : String = getString(context, R.string.name)
    if (name.isEmpty()) {
        return SignUpResponse.NameError(context.getString(R.string.error_empty_masculine, nameConst))
    }
    return SignUpResponse.Success
}

fun validateLastName(context: Context, lastName: String): SignUpResponse {
    val lastNameConst : String = getString(context, R.string.last_name)
    if (lastName.isEmpty()) {
        return SignUpResponse.LastNameError(context.getString(R.string.error_empty_masculine, lastNameConst))
    }
    return SignUpResponse.Success
}

fun validatePhone(context: Context, phone: String): SignUpResponse {
    val phoneConst : String = getString(context, R.string.phone)
    if (phone.isNotEmpty()) {
        if (!phoneRegex.matches(phone)) {
            SignUpResponse.PhoneError(context.getString(R.string.error_format, phoneConst))
        }
    }
    return SignUpResponse.Success
}

fun validateEmail(context: Context, email: String): LoginResponse {
    val emailConst : String = getString(context, R.string.email)
    if (email.isEmpty()) {
        return LoginResponse.EmailError(context.getString(R.string.error_empty_masculine, emailConst))
    } else if (!emailRegex.matches(email)) {
        return LoginResponse.EmailError(context.getString(R.string.error_format, emailConst))
    }

    return LoginResponse.Success
}

fun validatePassword(context: Context, password: String): LoginResponse {
    val passwordConst : String = getString(context, R.string.password)
    if (password.isNullOrEmpty()) {
        return LoginResponse.PasswordError(context.getString(R.string.error_empty_feminine, passwordConst))
    } else if (password.length < 6) {
        return LoginResponse.PasswordError(context.getString(R.string.error_password_too_short))
    }

    return LoginResponse.Success
}

