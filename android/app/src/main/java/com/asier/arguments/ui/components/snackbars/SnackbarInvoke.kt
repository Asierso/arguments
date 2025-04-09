package com.asier.arguments.ui.components.snackbars

/**
 * Used to interact with SnackbarHost. Allows to invoke every snackbar type
 * Ever SnackbarInvoke is build with this format: >{type}:{message}
 */
data class SnackbarInvoke(val type: SnackbarType, var message : String = "") {
    fun build() : String{
        return ">${type.getType()}:${message}"
    }
    companion object {
        fun from(builtInvoke : String) : SnackbarInvoke?{
            if(!builtInvoke.startsWith(">") || !builtInvoke.contains(":"))
                return null
            val params = builtInvoke.split(":")

            //Build snackbar invoke
            return SnackbarType.fromString(params[0].substring(1))
                ?.let { SnackbarInvoke(it, params.subList(1,params.size-1).joinToString()) }
        }
    }
}