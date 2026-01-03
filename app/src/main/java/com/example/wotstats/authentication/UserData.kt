package com.example.wotstats.authentication

data class UserData(
    val userId: String,
    val username: String?,
    val isNewUser: Boolean?,
    val profilePictureUrl: String?,
)
