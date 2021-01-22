package ru.hse.miem.miemapp.presentation

interface OnBackPressListener {
    /**
     * @return true if action was consumed, otherwise false
     */
    fun onBackPressed(): Boolean
}