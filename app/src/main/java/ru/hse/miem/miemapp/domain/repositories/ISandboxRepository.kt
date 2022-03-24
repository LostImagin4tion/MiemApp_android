package ru.hse.miem.miemapp.domain.repositories

import ru.hse.miem.miemapp.domain.entities.ProjectInSandbox

interface ISandboxRepository {
    suspend fun getSandboxProjects(): List<ProjectInSandbox>
}