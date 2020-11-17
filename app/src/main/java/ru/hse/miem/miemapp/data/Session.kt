package ru.hse.miem.miemapp.data

class Session {
    @Volatile var token: String = ""
        set(value) {
            if (field.isEmpty()) field = value
            else throw IllegalAccessException("Already initialized!")
        }
    @Volatile var email: String = ""
        set(value) {
            if (field.isEmpty()) field = value
            else throw IllegalAccessException("Already initialized!")
        }
    @Volatile var isStuff: Boolean = false
        set(value) {
            if (!field) field = value
            else throw IllegalAccessException("Already initialized!")
        }
    @Volatile var isStudent: Boolean = false
        set(value) {
            if (!field) field = value
            else throw IllegalAccessException("Already initialized!")
        }

    override fun toString() = "Session{token=*censored*, email=$email, isStuff=$isStuff, isStudent=$isStudent}"
}