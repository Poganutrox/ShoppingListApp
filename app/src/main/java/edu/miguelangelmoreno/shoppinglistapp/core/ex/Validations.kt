package edu.miguelangelmoreno.shoppinglistapp.core.ex

import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginResponse
import edu.miguelangelmoreno.shoppinglistapp.ui.signup.SignUpResponse

private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
private val phoneRegex = Regex("^[6789]\\d{8}$")

fun validatePasswordRepeated(password: String, passwordRepeated: String): SignUpResponse {
    if(passwordRepeated != password){
        return SignUpResponse.RepeatPasswordError("Las contraseñas no coinciden")
    }
    return SignUpResponse.Success
}

fun validateName(name: String) : SignUpResponse{
    if(name.isEmpty()){
        return SignUpResponse.NameError("El nombre no puede estar vacios")
    }
    return SignUpResponse.Success
}
fun validateLastName(lastName: String) : SignUpResponse{
    if(lastName.isEmpty()){
        return SignUpResponse.LastNameError("Los apellidos no pueden estar vacios")
    }
    return SignUpResponse.Success
}
fun validatePhone(phone: String) : SignUpResponse {
    if (phone.isNotEmpty()){
        if (!phoneRegex.matches(phone)) {
            SignUpResponse.PhoneError("Formato de teléfono no válido")
        }
    }
    return SignUpResponse.Success
}

fun validateEmail(email: String) : LoginResponse {
    if( email.isEmpty()){
        return LoginResponse.EmailError("El email no puede estar vacio")
    }
    else if(!emailRegex.matches(email)){
        return LoginResponse.EmailError("El formato del email no es correcto")
    }

    return LoginResponse.Success
}

fun validatePassword(password: String) : LoginResponse{
    if(password.isNullOrEmpty()){
        return LoginResponse.PasswordError("La contraseña no puede estar vacia")
    }
    else if(password.length < 6){
        return LoginResponse.PasswordError("La contraseña debe contener al menos 6 valores")
    }

    return LoginResponse.Success
}