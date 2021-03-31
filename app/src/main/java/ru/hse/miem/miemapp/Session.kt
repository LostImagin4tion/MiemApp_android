package ru.hse.miem.miemapp

class Session {
    @Volatile var token: String = ""
    @Volatile var email: String = ""
    @Volatile var isStuff: Boolean = false
    @Volatile var isStudent: Boolean = false

    fun reset() {
        token = ""
        email = ""
        isStuff = false
        isStudent = false
    }

    override fun toString() = "Session{token=*censored*, email=$email, isStuff=$isStuff, isStudent=$isStudent}"
}