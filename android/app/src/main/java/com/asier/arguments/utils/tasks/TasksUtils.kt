package com.asier.arguments.utils.tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object TasksUtils {
    fun <T> executeAtCron(
        scope: CoroutineScope,
        action: Task<T>,
        delay: Long,
        unit: TimeUnit = TimeUnit.SECONDS,
        parameters: T? = null
    ) {
        scope.launch(Dispatchers.IO) {
            var repetable = true
            while (repetable && isActive) {
                repetable = action.run(parameters)
                if (repetable)
                    delay(unit.toMillis(delay))
            }
        }
    }
}