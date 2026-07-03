package com.example.core.domain.util

class PaginationErrorException(
    val error: DataError
) : Exception()