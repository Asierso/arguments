package com.asier.arguments.utils.tasks

interface Task<T> {
    suspend fun run(param: T?) : Boolean
}