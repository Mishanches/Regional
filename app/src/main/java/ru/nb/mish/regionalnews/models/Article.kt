package ru.nb.mish.regionalnews.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) // игнорируем ненужные параметры с сервера (автор и пр.)

// Parcelable - для преобразования объекта Article, т.к. мы позже будем передаать статью в интенте, а
// интент пинимает (в putExtra) только примитивные типы

class Article() : Parcelable {

    lateinit var title: RenderedSrtring
    lateinit var excerpt: RenderedSrtring // мини-описание
    // используем только для того, чтобы "выдернуть" из нее первую картинку (а так - у нас WebView который сам все отобразит)
    lateinit var content: RenderedSrtring
    @get: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // указываем формат даты, который на сервере
    lateinit var date: Date

    // вторичный конструктор нужен, чтобы передать из AllArticleActivity передать статью в ArticleActivity
    constructor(parcel: Parcel) : this() {
        title = RenderedSrtring(parcel.readString()) // 2. А затем полученные данные присваиваем переменным
        excerpt = RenderedSrtring(parcel.readString())
        content = RenderedSrtring(parcel.readString())
        date = Date(parcel.readLong()) // записываем дату в милисикундах
    }

    //метод для парсинга контента и "выдергивания" из нее первой картинки
    // затем этот метод используется в Glide в ArticleAdapter

    fun getImageUrl(): String? {
        val matchGroup =  Regex("src=\"(.*jpg||.*jpeg)\"").find(content.toString())?.groups
        return if (matchGroup?.size==2) matchGroup[1]?.value else "android.resource://ru.nb.mish.regionalnews/drawable/ic_no_image"
    }

    // вызываем метод writeToParcel, который записывает все переменные, а затем
    // в затем эти двнные передвются в интенте в AllArticleActivity (там есть it - это и есть обхъект Article)
    // и в ArticleActivity мы принимаем эти данные строчкой val article = intent.getParcelableExtra<Article>
    // т.е. читает эти данные, которые мы тут записали

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title.toString()) // 1. Сначала записываем данные - считываем из Parcelable
        parcel.writeString(excerpt.toString())
        parcel.writeString(content.toString())
        parcel.writeLong(date.time) // превращаем милисикунды обратно в дату

    }

    // ниже методы - по умолчнаию, когда наследуемся от Parcelable
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}