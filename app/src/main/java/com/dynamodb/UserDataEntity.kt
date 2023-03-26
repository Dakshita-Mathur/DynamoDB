package com.dynamodb

data class UserDataEntity (
    val pk: String,
    val loginId: String,
    val userName: String,
    val email: String,
    val password: String,
)