package edu.miguelangelmoreno.shoppinglistapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.service.APIService
import edu.miguelangelmoreno.shoppinglistapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val authToken = userPrefs.getToken()
                chain.proceed(chain.request().newBuilder().also {
                    if (authToken != null) {
                        it.addHeader("Authorization", "Bearer $authToken")
                    }
                }.build())
            }.build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }
}