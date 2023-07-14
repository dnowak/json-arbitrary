package io.github.dnowak.json

data class ElementProperty(val name: String, val value: Json)

sealed interface Json {
    data class JsonString(val value: String): Json
    data class JsonNumber(val value: Int): Json
    data object JsonNull: Json
    data class JsonBool(val value: Boolean): Json
    data class JsonArray(val value: List<Json>): Json
    data class JsonElement(val value: List<ElementProperty>): Json
}

fun main() {
    val element = Json.JsonElement(
        listOf(
            ElementProperty("name", Json.JsonString("Dariusz")),
            ElementProperty("id", Json.JsonNumber(102))
        )
    )

    println(element)
}
