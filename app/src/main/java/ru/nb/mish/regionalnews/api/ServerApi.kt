package ru.nb.mish.regionalnews.api

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Url
import ru.nb.mish.regionalnews.models.Article

interface ServerApi {
@GET
fun loadNews (@Url url: String): Deferred<List<Article>>

}