package com.silas.themovies.data.source.remote.client

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Initializes the Client that will make service requests for the App
 *
 * @property API_KEY API key for certain service calls
 *
 * @author silas.silva in 23/02/2020
 * */
object ClientService {

    /**
     * Create and return a Service of the specified type, with the base request url and create
     * a Client with this data
     *
     * @param T type that specifies the service to be configured
     * @param url url base for service flames
     * @return return of requested service type already configured
     * */
    internal inline fun <reified T> createNewService(url: String): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(setUpClientHttp())
            .addConverterFactory(configureFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(T::class.java)
    }

    /**
     * Here is configured the http client as well as an interceptor for debug logging
     *
     * @return returns client of type OkHttpClient
     * */
    internal fun setUpClientHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    /**
     * In this method the class responsible for factoring the JSONs received
     * by the server is configured.
     *
     * @return returns GsonConverterFactory converter instance
     * */
    private fun configureFactory(): GsonConverterFactory {
        val factoryBuilder = GsonBuilder().create()
        return GsonConverterFactory.create(factoryBuilder)
    }
}