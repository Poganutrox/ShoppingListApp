package edu.miguelangelmoreno.shoppinglistapp.model.responses

data class ApiResponse<T>(
    var success: Boolean,
    var result: T? = null,
    var error: String? = null
)
