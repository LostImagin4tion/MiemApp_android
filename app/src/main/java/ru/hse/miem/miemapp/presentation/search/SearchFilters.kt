package ru.hse.miem.miemapp.presentation.search

data class SearchFilters(
    var projectType: Int = 0,
    var projectTypeName: String = "",

    var projectState: Int = 0,
    var projectStateName: String = "",

    var isAvailableVacancies: Boolean = false
)