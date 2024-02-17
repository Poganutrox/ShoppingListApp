package edu.miguelangelmoreno.shoppinglistapp.core.ex

private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
private val phoneRegex = Regex("^[6789]\\d{8}$")

fun validatePasswordRepeated(password: String, passwordRepeated: String): Boolean {
    return passwordRepeated == password
}

fun validateName(name: String) : Boolean{
    return name.isNotEmpty()
}
fun validationLastName(lastName: String) : Boolean{
    return lastName.isNotEmpty()
}
fun validatePhone(phone: String) : Boolean{
    if(phone.isNotEmpty())
        return !phoneRegex.matches(phone)
    else
        return true
}

fun validateEmail(email: String) : Boolean {
    return !(!emailRegex.matches(email) || email.isEmpty())
}

fun validatePassword(password: String) : Boolean{
    return !password.isNullOrEmpty() && password.length >= 6
}