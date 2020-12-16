package ru.hse.miem.miemcam.dagger

import ru.hse.miem.miemcam.presentation.cameras.CamerasListFragment
import ru.hse.miem.miemcam.presentation.control.ControlPanelFragment
import ru.hse.miem.miemcam.presentation.main.CamerasActivity
import ru.hse.miem.miemcam.presentation.record.RecordFragment
import ru.hse.miem.miemcam.presentation.vmix.VmixControlFragment
import dagger.Component
import ru.hse.miem.miemapp.dagger.AppComponent
import javax.inject.Scope

@CamerasScope
@Component(dependencies = [AppComponent::class], modules = [DataModule::class, RepositoryModule::class])
interface CamerasControlComponent {
  fun inject(camerasActivity: CamerasActivity)
  fun inject(camerasListFragment: CamerasListFragment)
  fun inject(controlPanelFragment: ControlPanelFragment)
  fun inject(recordFragment: RecordFragment)
  fun inject(vmixControlFragment: VmixControlFragment)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class CamerasScope