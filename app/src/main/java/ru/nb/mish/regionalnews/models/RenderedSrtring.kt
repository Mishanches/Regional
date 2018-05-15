package ru.nb.mish.regionalnews.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


// клас для упрощения написания Article.kt, т.к. все параметры на сервера завернуты в rendered
// если это упращение не делать, то в Article.kt нужно писать все время renderd.title вместо просто title

@JsonIgnoreProperties(ignoreUnknown = true) // игнорируем ненужные параметры с сервера (автор и пр.)

// в Article.kt мы указываем параметры, например, title: RenderedSrtring
class RenderedSrtring() { // () - Jakson вызывает сначала пустой конструктор
    lateinit var rendered:String
    override fun toString(): String {
        return rendered
    }

    // создаем второй конструктор для того, чтобы считывать из Parceable и записыываать в переменную
    constructor(value: String): this() {
        rendered = value
    }
}