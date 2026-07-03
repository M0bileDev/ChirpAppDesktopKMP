package com.example.core.domain.validation

object PasswordValidator {

    private const val MIN_PASSWORD_LENGTH = 9

    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= 9,
            hasDigit = password.any { character -> character.isDigit() },
            hasUppercase = password.any { character -> character.isUpperCase() }
        )
    }
}