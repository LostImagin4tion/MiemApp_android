package ru.hse.miem.miemapp.presentation.sandbox

data class SandboxFilters(
    var projectType: Int = 0,
    var projectTypeName: String = "",

    var projectState: Int = 0,
    var projectStateName: String = "",

    var isAvailableVacancies: Boolean = false
)