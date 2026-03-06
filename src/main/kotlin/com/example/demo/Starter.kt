package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class Starter(private val stolovayService: StolovayService) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(Starter::class.java)

    override fun run(vararg args: String) {
        // stolovay1 уже внедрён, не нужно создавать новый
        stolovayService.start()
        stolovayService.start()
        val newDish = Dish("soup", "tasty", 100)
        logger.info("try to create new dish = ${newDish.name}")
        stolovayService.addDish(newDish)
        println(stolovayService.findByName("soup"))
        stolovayService.ChangePriceByName("soup",24000001)
        stolovayService.delete("soup")
        println(stolovayService.findByName("soup"))
    }
}