package ru.hse.miem.miemcam

import dagger.Component
import ru.hse.miem.miemapp.dagger.AppComponent
import javax.inject.Scope

@CamerasScope
@Component(dependencies = [AppComponent::class])
interface CamerasControlComponent {

    fun inject(camerasActivity: CamerasActivity)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class CamerasScope