package com.example.SpringhomeWork

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

interface TypeOfSerivce {
    fun printName()
    fun lozung()
}

@Component
@ConditionalOnProperty(name = ["service.type"], havingValue = "China")
class ChinaServiceType : TypeOfSerivce {
    private val logger = LoggerFactory.getLogger(ChinaServiceType::class.java)
    override fun printName() {
        println("ва саун нима Ресторан")
        logger.info("Это КИТАЙСКИЙ сервис (лучший в мире )")
    }
    override fun lozung() {println("""
        CHINA NUMBER 
         ONE!!!   ONE!!!   ONE!!!   ONE!!!   ONE!!!
            ONE!!!   ONE!!!   ONE!!!   ONE!!!   
         ONE!!!   ONE!!!   ONE!!!   ONE!!!   ONE!!!  
            ONE!!!   ONE!!!   ONE!!!   ONE!!!   
    """.trimIndent())
    }
}



@Component
@ConditionalOnProperty(name = ["service.type"], havingValue = "Russia")
class RussiaServiceType : TypeOfSerivce {
    private val logger = LoggerFactory.getLogger(ChinaServiceType::class.java)
    override fun printName() {
        println("Столовая ")
        logger.info("Это Российский сервис (2ой лучший в мире ;3c )")
    }
    override fun lozung() {println("""
        Мороз и солнце; день чудесный!
        Еще ты дремлешь, друг прелестный —
        Пора, красавица, проснись:
        Открой сомкнуты негой взоры
        Навстречу северной Авроры,
        Звездою севера явись!
    """.trimIndent())
    }
}



data class Dish(
    val name: String,
    val description: String? = null,
    var price: Int
)

@Service
class StolovayService(private val servicetype: TypeOfSerivce?= null) {
    private val logger = LoggerFactory.getLogger(StolovayService::class.java)
    private val dishes = mutableListOf<Dish>()

    fun start() {
        servicetype?.printName()
        servicetype?.lozung()
    }
    fun addDish(dish: Dish) {
        dishes.add(dish)
        logger.info("Added dish: {} with price {}", dish.name, dish.price)
    }

    fun delete(nameOfDish: String): Boolean {
        val foundDish = dishes.find { it.name == nameOfDish }

        return if (foundDish != null) {
            dishes.remove(foundDish)
            logger.info("Deleted rdish: {}", nameOfDish)
            true
        } else {
            logger.warn("Attempted to delete non-existent dish: {}", nameOfDish)
            false
        }
    }

    fun findByName(name: String): String? {
        logger.debug("Searching for dish: {}", name)
        return dishes.find { it.name.equals(name, ignoreCase = true) }?.name ?: "Didnt find"
    }

    fun ChangePriceByName(name: String,newPrice: Int){
        logger.debug("Searching for dish to change price: {}", name)
        dishes.firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?.apply {
                price = newPrice
                logger.info("Price for '{}' updated to {}", name, newPrice)
            } ?: logger.warn("Couldn't find - dish: '{}'  ", name)
    }


    fun getAllDishes(): List<Dish> = dishes.toList()
}