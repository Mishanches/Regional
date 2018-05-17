package ru.nb.mish.regionalnews.models

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) // игнорируем ненужные параметры с сервера (автор и пр.)

//  преобразовываем объект Article

class Article() : Parcelable {

    lateinit var title: RenderedSrtring
    // мини-описание
    lateinit var excerpt: RenderedSrtring
    lateinit var content: RenderedSrtring
    // указываем формат даты, который на сервере
    @get: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    lateinit var date: Date

    constructor(parcel: Parcel) : this() {
        title = RenderedSrtring(parcel.readString())
        excerpt = RenderedSrtring(parcel.readString())
        content = RenderedSrtring(parcel.readString())
        date = Date(parcel.readLong())
    }

    // берем первую картинку из статьи
    fun getImageUrl(): String? {
        val matchGroup =  Regex("src=\"(.*jpg||.*jpeg)\"").find(content.toString())?.groups
        return if (matchGroup?.size==2) matchGroup[1]?.value
        else "android.resource://ru.nb.mish.regionalnews/drawable/ic_no_image"
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title.toString())
        parcel.writeString(excerpt.toString())
        parcel.writeString(content.toString())
        parcel.writeLong(date.time)

    }

    // методы, наследуемые от Parcelable
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