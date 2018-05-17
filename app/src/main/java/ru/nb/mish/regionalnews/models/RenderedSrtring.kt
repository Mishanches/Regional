package ru.nb.mish.regionalnews.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


// для упрощения написания, т.к. все параметры на сервера завернуты в rendered

@JsonIgnoreProperties(ignoreUnknown = true)


class RenderedSrtring() {
    lateinit var rendered:String
    override fun toString(): String {
        return rendered
    }

    constructor(value: String): this() {
        rendered = value
    }
}