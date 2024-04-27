package edu.miguelangelmoreno.shoppinglistapp.model.responses

data class PageResponse<T>(
    val content: List<T>,
    val last: Boolean,
    val totalElements: Int,
    val totalPages: Int,
    val first: Boolean,
    val size: Int,
    val number: Int,
    val numberOfElements: Int,
    val empty: Boolean
)