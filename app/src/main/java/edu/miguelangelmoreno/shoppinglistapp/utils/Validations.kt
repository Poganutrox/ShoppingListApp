package edu.miguelangelmoreno.shoppinglistapp.utils

import android.content.Context
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
    if (name.isEmpty()) {
        return SignUpResponse.NameError(context.getString(R.string.error_name_empty))
    }
    return SignUpResponse.Success
}

fun validateLastName(context: Context, lastName: String): SignUpResponse {
    if (lastName.isEmpty()) {
        return SignUpResponse.LastNameError(context.getString(R.string.error_last_name_empty))
    }
    return SignUpResponse.Success
}

fun validatePhone(context: Context, phone: String): SignUpResponse {
    if (phone.isNotEmpty()) {
        if (!phoneRegex.matches(phone)) {
            SignUpResponse.PhoneError(context.getString(R.string.error_invalid_phone_format))
        }
    }
    return SignUpResponse.Success
}

fun validateEmail(context: Context, email: String): LoginResponse {
    if (email.isEmpty()) {
        return LoginResponse.EmailError(context.getString(R.string.error_email_empty))
    } else if (!emailRegex.matches(email)) {
        return LoginResponse.EmailError(context.getString(R.string.error_invalid_email_format))
    }

    return LoginResponse.Success
}

fun validatePassword(context: Context, password: String): LoginResponse {
    if (password.isNullOrEmpty()) {
        return LoginResponse.PasswordError(context.getString(R.string.error_password_empty))
    } else if (password.length < 6) {
        return LoginResponse.PasswordError(context.getString(R.string.error_password_too_short))
    }

    return LoginResponse.Success
}

