package io.github.dnowaj.json

import io.github.dnowak.json.Json
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll

class ArbitraryKtTest : FreeSpec({
    "Arbitrary for JsonNull" {
        val jsonNullArb: Arb<Json.JsonNull> = Arb.json()
        jsonNullArb.checkAll(iterations = 2) {
            println(it)
        }
    }
    "Arbitrary for JsonBool" {
        val jsonBoolArb: Arb<Json.JsonBool> = Arb.json()
        jsonBoolArb.checkAll(iterations = 4) {
            println(it)
        }
    }
    "Arbitrary for JsonString" {
        val jsonStringArb: Arb<Json.JsonString> = Arb.json()
        jsonStringArb.checkAll(iterations = 10) {
            println(it)
        }
    }
    "Arbitrary for JsonArray" {
        val jsonArrayArb: Arb<Json.JsonArray> = Arb.json()
        jsonArrayArb.checkAll(iterations = 10) {
            println(it)
        }
    }
    "Arbitrary for JsonElement" {
        val jsonElementArb: Arb<Json.JsonElement> = Arb.json()
        jsonElementArb.checkAll(iterations = 10) {
            println(it)
        }
    }
})