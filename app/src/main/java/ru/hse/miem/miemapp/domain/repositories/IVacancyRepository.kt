package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Single
import ru.hse.miem.miemapp.domain.entities.Vacancies

interface IVacancyRepository {
    fun getAllVacancies(): Single<List<Vacancies>>
}