package com.asier.arguments.utils

import android.app.Service
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.pages.PageResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.LocalDateTime

object GsonUtils {
    val gson = GsonBuilder()
        .registerTypeAdapter(Instant::class.java, JsonDeserializer { json, _, _ ->
            Instant.parse(json.asString)
        }).create()

    inline fun <reified T> jsonToClass(result: LinkedTreeMap<*, *>) : T{
        val resultJson = gson.toJson(result)
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson<T>(resultJson, type)
    }




}