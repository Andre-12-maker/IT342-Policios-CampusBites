package com.cit.policios.campusbites.features.auth

data class User(
    var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var role: String? = null
)
