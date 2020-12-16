package ru.hse.miem.miemcam


class CameraSession {

  var key = "9445980805164d5aa0efc6c91500d298"

  companion object {
    val basicAdressNvr = "https://nvr.miem.hse.ru/api/"
    var basicAdress = "http://19111.miem.vmnet.top"
    val defaultVmix = "172.18.191.12:8088"
  }

  fun clean() {
    pickedRoom = ""
    pickedCamera = ""
  }

  var pickedRoom = ""
  var pickedCamera = ""
}