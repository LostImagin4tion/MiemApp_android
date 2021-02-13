package ru.hse.miem.tinder.presentation.cards

class ItemModel() {
    private var image: Int = 0
    private var nama: String = ""
    private var usia: String = ""
    private var kota: String = ""

    constructor(_image: Int, _nama: String, _usia: String, _kota: String) : this() {
        image = _image
        nama = _nama
        usia = _usia
        kota = _kota
    }

    public fun getImage(): Int = image;

    public fun getNama(): String = nama;

    public fun getUsia(): String = usia;

    public fun getKota(): String = kota;
}