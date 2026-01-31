package com.nanoporetech.scainternew.model

data class AppUiState(
    /** isLoggedIn tracks whether the user is currently logged in **/
    val isLoggedIn: Boolean = false,
    /** isLoginInvalid tracks whether the user supplied credentials are valid **/
    val isLoginInvalid: Boolean = false,
    /** username is the username value entered by the user **/
    val username: String = "",
    /** username is the password value entered by the user **/
    val password: String = "",
    /** rememberMe tracks whether the user wants to be remembered **/
    val rememberMe: Boolean = false
)
