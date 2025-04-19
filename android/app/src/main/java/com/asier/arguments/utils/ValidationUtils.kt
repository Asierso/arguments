package com.asier.arguments.utils

import com.asier.arguments.misc.PasswordPolicyCodes
import org.apache.commons.lang3.StringUtils

object ValidationUtils {
    fun checkPasswords(password1 : String, password2 : String): PasswordPolicyCodes {
        if (password1.length < 6)
            return PasswordPolicyCodes.TOO_SHORT
        if (StringUtils.containsAny(password1, "/$\"'?¿¡!·`[]{}()\\| "))
            return PasswordPolicyCodes.INVALID
        if (password1 != password2)
            return PasswordPolicyCodes.NOT_EQUALS
        if (StringUtils.isAllLowerCase(password1) ||
            StringUtils.isAllUpperCase(password1) ||
            StringUtils.isAlpha(password1) ||
            StringUtils.isNumeric(password1)
        )
            return PasswordPolicyCodes.WEAK
        return PasswordPolicyCodes.STRONG
    }
}