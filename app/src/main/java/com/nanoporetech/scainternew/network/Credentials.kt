package com.nanoporetech.scainternew.network

data class Credentials(
    val username: String,
    val password: String
) {
    fun isEmpty(): Boolean {
        return username.isEmpty() or password.isEmpty()
    }
}
