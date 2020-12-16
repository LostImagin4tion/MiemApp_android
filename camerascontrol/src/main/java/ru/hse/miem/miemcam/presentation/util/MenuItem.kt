package ru.hse.miem.miemcam.presentation.util

import ru.hse.miem.miemcam.R

enum class FragmentType {
  CONTROL,
  RECORD,
  VMIX
}

class MenuItem(val fragment: FragmentType) {

  val icon: Int
    get() {
      when (fragment) {
        FragmentType.CONTROL -> return R.drawable.ic_control_icon
        FragmentType.RECORD -> return R.drawable.ic_record_icon
        FragmentType.VMIX -> return R.drawable.ic_vmix_icon
      }
    }

  val name: Int
    get() {
      when (fragment) {
        FragmentType.CONTROL -> return R.string.title_control_panel
        FragmentType.RECORD -> return R.string.title_record
        FragmentType.VMIX -> return R.string.title_vmix
      }
    }
}