package ru.nb.mish.regionalnews.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    private val BASE_URL = "http://knia.ru/wp-json/wp/v2/"

    private val httpClient = OkHttpClient.Builder() // конфигурируем http запрос
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            // .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // расширенные логи

    val retrofit = Retrofit.Builder() // запрос к серверу через Retrofit
            .client(httpClient.build())
            .baseUrl(BASE_URL)
            // парсим данные
            .addConverterFactory(JacksonConverterFactory.create( // Jackson - конвертируем JSON с сревера в объект Article
                    ObjectMapper().registerModule(SimpleModule())
            ))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())  // Корутины (асинхронные запросы к серверу)
            .build()

    val serverApi: ServerApi
    get() = retrofit.create(ServerApi::class.java)

}