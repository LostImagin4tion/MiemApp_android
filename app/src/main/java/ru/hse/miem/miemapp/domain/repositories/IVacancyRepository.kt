package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.Vacancies

interface IVacancyRepository {
    suspend fun getAllVacancies(): List<Vacancies>
}