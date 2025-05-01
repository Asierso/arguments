package com.asier.arguments.ui.components.snackbars

/**
 * Used to interact with SnackbarHost. Represents every type of snackbar that will be used
 */
enum class SnackbarType(private var type: String) {
    SERVER_ERROR("server-error"),SUCCESS("success"),WARNING("warning");
    fun getType() : String{
        return type
    }
    companion object {
        fun fromString(type: String): SnackbarType? {
            return entries.find { it.type == type }
        }
    }
}