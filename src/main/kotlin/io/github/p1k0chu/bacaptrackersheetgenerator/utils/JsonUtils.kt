package io.github.p1k0chu.bacaptrackersheetgenerator.utils

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

val JsonElement.minecraftText: String
    get() {
        if (this is JsonObject)
            get("translate")?.let { return it.jsonPrimitive.content }
        return jsonPrimitive.content
    }