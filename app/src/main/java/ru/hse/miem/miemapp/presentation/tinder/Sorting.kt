package ru.hse.miem.miemapp.presentation.tinder

import ru.hse.miem.miemapp.domain.entities.VacancyCard
import ru.hse.miem.miemapp.domain.entities.Vacancies
import ru.hse.miem.miemapp.domain.entities.tagsList
import java.util.ArrayList

// TO-DO написать метод для добавления понравившихся вакансий или посмотреть SET

class Sorting {
    companion object {
        var roles: MutableMap<String, Int> = mutableMapOf()
        var categories: MutableMap<String, Int> = mutableMapOf()
        var position: Int = 0
        var count: Int = 0
        var likeIndexes: ArrayList<Int> = arrayListOf()
        var likeVacancies: ArrayList<VacancyCard> = arrayListOf()
    }

    fun plus(){
        count++
    }

    private fun getAmount(role: String): Int = roles[role]!!

    private fun getCount(category: String): Int = categories[category]!!

    fun add(role: String, amount: Int){
        if (role in tagsList){
            categories[role] = amount
        }
        roles[role] = amount
    }

    fun add(role: String){
        if (role == ""){

        }else if (roles.contains(role)){
            roles[role] = getAmount(role) + 1
        }else{
            roles[role] = 1
        }
    }

//    fun add(item: VacancyCard){
//        if (roles.contains(item.vacancy)){
//            roles[item.vacancy] = getAmount(item.vacancy) + 1
//        }else{
//            roles[item.vacancy] = 1
//        }
//
//        for (temp_tag in tagsList) {
//            if (item.requirements.indexOf(temp_tag, ignoreCase = true) != -1) {
//                if (categories.contains(temp_tag)){
//                    categories[temp_tag] = getCount(temp_tag) + 1
//                }else{
//                    categories[temp_tag] = 1
//                }
//            }
//        }
//    }

    fun getRoles(): MutableMap<String, Int>{
        return roles
    }

    fun clear(){
        roles.clear()
        categories.clear()
        likeIndexes.clear()
    }

    fun sort(items: List<VacancyCard>): ArrayList<VacancyCard> {
        val sortItems: ArrayList<VacancyCard> = arrayListOf()
        var c = count

        if (c == 0){
            c = 1
        }
        for (i in c..items.size-2){
            if (items[i] in likeVacancies){
                continue
            }
            if (!roles.keys.contains(items[i].vacancy_role)){
                sortItems.add(items[i])
            }else{
                val size = sortItems.size
                for (j in sortItems.indices){
                    if (roles.keys.contains(sortItems[j].vacancy_role) && roles[items[i].vacancy_role]!! > roles[sortItems[j].vacancy_role]!!){
                        sortItems.add(j, items[i])
                        break
                    }
                }
                if (size >= sortItems.size){
                    sortItems.add(size, items[i])
                }
            }
        }

        if (count == 0) {
            sortItems.add(0, items[0])
            sortItems.add(items[items.size - 1])
        }
        return sortItems
    }
}