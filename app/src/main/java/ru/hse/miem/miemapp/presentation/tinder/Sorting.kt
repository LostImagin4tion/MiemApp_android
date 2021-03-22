package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log

class Sorting {
    companion object {
        var roles: MutableMap<String, Int> = mutableMapOf()
        var position: Int = 0
        var count: Int = 0
    }

    fun plus(){
        count++
    }

    private fun getAmount(role: String): Int = roles[role]!!

    fun add(role: String, amount: Int){
        roles[role] = amount
    }

    fun add(role: String){
        if (roles.contains(role)){
            roles[role] = getAmount(role) + 1
        }else{
            roles[role] = 1
        }
    }

    fun getRoles(): MutableMap<String, Int>{
        return roles
    }

    fun clear(){
        roles.clear()
    }
}