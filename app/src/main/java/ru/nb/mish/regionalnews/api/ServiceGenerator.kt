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
            .connectTimeout(10, TimeUnit.SECONDS) // коннектимся 10 сек, затем разрываем соединение
            .readTimeout(10, TimeUnit.SECONDS) // читаем
            .writeTimeout(10, TimeUnit.SECONDS) // записываем данные
            // .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // расширенные логи

            // устанавливаем retrofit - библиотека жоступа к сети
            // исользуем ее совместно с OkHttpClient (выше)

    val retrofit = Retrofit.Builder() // создаем ретрофит-клиент, который создает запрос к серверу
            .client(httpClient.build()) // клмент для HTTP-запроса
            .baseUrl(BASE_URL)
            // далее парсим данные
            .addConverterFactory(JacksonConverterFactory.create( // библиотека Jackson для конвертирования объектов JSON с сревера
                    // в объект Article
                    ObjectMapper().registerModule(SimpleModule()) //тут внктренние системные классы для правильной конвертации
            ))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())  //  включаем Корутины - асинхронные запросы к серверу
            .build() // все, что написали, выстраиваем

    val serverApi: ServerApi // ServerApi - это наш интерфейс, который делает запросы и вызывает метод loadNews
    get() = retrofit.create(ServerApi::class.java) //

}