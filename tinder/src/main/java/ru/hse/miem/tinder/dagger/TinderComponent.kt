package ru.hse.miem.tinder.dagger

import dagger.Component
import ru.hse.miem.miemapp.dagger.AppComponent
import ru.hse.miem.tinder.presentation.main.TinderActivity
import javax.inject.Scope

@Tinder
@Component(dependencies = [AppComponent::class], modules = [DataModule::class])
interface TinderComponent {
    fun inject(tinderActivity: TinderActivity)
}
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Tinder
