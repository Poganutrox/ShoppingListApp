package edu.miguelangelmoreno.shoppinglistapp.data.repository

import edu.miguelangelmoreno.shoppinglistapp.data.datasource.APIDataSource
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val dataSource: APIDataSource
){
    fun getCategories(): Flow<Response<List<Category>>> = flow {
        emit(dataSource.getCategories())
    }
}