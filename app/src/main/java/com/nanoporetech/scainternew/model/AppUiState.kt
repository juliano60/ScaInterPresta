package com.nanoporetech.scainternew.model

data class AppUiState(
    // isLoggedIn tracks whether the user is currently logged in
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val password: String = ""
)
