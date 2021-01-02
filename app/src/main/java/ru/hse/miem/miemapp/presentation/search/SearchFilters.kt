package ru.hse.miem.miemapp.presentation.search

data class SearchFilters(
    var projectsType: Int = 0,
    var projectsTypeName: String = "",
    var isAvailableVacancies: Boolean = false
)