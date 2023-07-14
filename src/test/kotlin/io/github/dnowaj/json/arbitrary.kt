package io.github.dnowaj.json

import io.github.dnowak.json.ElementProperty
import io.github.dnowak.json.Json
import io.github.dnowak.json.Json.*
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.reflect.KClass

fun <T : Any> arbitrarySealedSubclass(klass: KClass<T>): Arb<KClass<out T>> = Arb.element(klass.sealedSubclasses)
inline fun <reified J : Json> Arb.Companion.json(): Arb<J> = arbitraryJson(J::class)
@Suppress("UNCHECKED_CAST")
fun <J : Json> arbitraryJson(klass: KClass<J>): Arb<J> = when (klass) {
    JsonBool::class -> Arb.jsonBool() as Arb<J>
    JsonNull::class -> Arb.constant(JsonNull) as Arb<J>
    JsonNumber::class -> Arb.jsonNumber() as Arb<J>
    JsonArray::class -> {
        arbitrary {
            val valueClass = arbitrarySealedSubclass(Json::class).filterNot { it == JsonArray::class  }.bind()
            val arbValue: Arb<Json> = arbitraryJson(valueClass)
            Arb.list(arbValue, (5..10)).bind().let(::JsonArray)
        } as Arb<J>
    }
    JsonElement::class -> {
        val arbValue: Arb<Json> = arbitrary {
            val valueClass = arbitrarySealedSubclass(Json::class).bind()
            arbitraryJson(valueClass).bind()
        }
        arbitrary {
            Arb.map(Arb.string(2, 10), arbValue).bind()
                .map { (name, json) -> ElementProperty(name, json) }
                .toList()
                .let(::JsonElement)
            //TODO:: jsonElement(elements)
        } as Arb<J>
        Arb.constant(JsonElement(listOf(ElementProperty("constant", JsonString("value"))))) as Arb<J>
    }
    JsonString::class -> Arb.jsonString() as Arb<J>
    else -> throw IllegalArgumentException("Unsupported Json type: <$klass>")
}

fun Arb.Companion.jsonBool(arb: Arb<Boolean> = Arb.boolean()): Arb<JsonBool> = arb.map(::JsonBool)

fun Arb.Companion.jsonNumber(arb: Arb<Int> = Arb.int()): Arb<JsonNumber> = arb.map(::JsonNumber)

fun Arb.Companion.jsonString(arb: Arb<String> = Arb.string(1, 16, Codepoint.az())): Arb<JsonString> = arb.map(::JsonString)

fun Arb.Companion.jsonElement(elements: Map<String, Arb<Json>>): Arb<JsonElement> = arbitrary {
    elements
        .map { (key, arb) -> ElementProperty(key, arb.bind()) }
        .toList()
        .let(::JsonElement)
}

suspend fun main() {
    val jsonNullArb: Arb<JsonNull> = Arb.json()
    jsonNullArb.checkAll(iterations = 10) {
        println(it)
    }

    val jsonBoolArb: Arb<JsonBool> = Arb.json()
    jsonBoolArb.checkAll(iterations = 10) {
        println(it)
    }

    val jsonStringArb: Arb<JsonString> = Arb.json()
    jsonStringArb.checkAll(iterations = 10) {
        println(it)
    }

    val jsonArrayArb: Arb<JsonArray> = Arb.json()
    jsonArrayArb.checkAll(iterations = 10) {
        println(it)
    }

    /*
    val jsonElementArb: Arb<JsonElement> = Arb.json()
    jsonElementArb.checkAll {
        println(it)
    }

     */
}