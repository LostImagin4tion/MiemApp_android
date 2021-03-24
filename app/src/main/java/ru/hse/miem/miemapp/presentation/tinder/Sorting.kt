package ru.hse.miem.miemapp.presentation.tinder

import android.util.Log
import ru.hse.miem.miemapp.domain.entities.ItemModel
import java.util.ArrayList

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

    fun sort(items: List<ItemModel>): ArrayList<ItemModel> {
        val sortItems: ArrayList<ItemModel> = arrayListOf()

        for (i in 1..items.size-2){
            if (!roles.keys.contains(items[i].vacancy)){
                sortItems.add(items[i])
            }else{
                val size = sortItems.size
                for (j in sortItems.indices){
                    if (roles.keys.contains(sortItems[j].vacancy) && roles[items[i].vacancy]!! > roles[sortItems[j].vacancy]!!){
                        sortItems.add(j, items[i])
                        break
                    }
                }
                if (size >= sortItems.size){
                    sortItems.add(size, items[i])
                }
            }
        }
        sortItems.add(0, items[0])
        sortItems.add(items[items.size-1])
        return sortItems
    }
}