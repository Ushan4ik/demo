package com.example.demo.papka

import java.net.HttpURLConnection
import java.net.URL
import java.io.FileNotFoundException
import java.io.IOException
// ===========================================
// Задача 1. HTTP-запросы через HttpURLConnection
// ===========================================
// Цель: научиться отправлять GET и POST запросы, читать ответ и статус-код.
// API: https://jsonplaceholder.typicode.com
//
// TODO 1: Отправить GET /posts/1, вывести статус-код и тело ответа
// TODO 2: Отправить POST /posts с JSON-телом, вывести статус-код и тело
// TODO 3: Отправить GET /posts/9999, обработать ошибку (код != 2xx)
//
// Подсказки:
//   val connection = URL("...").openConnection() as HttpURLConnection
//   connection.requestMethod = "GET"             — задать метод
//   connection.doOutput = true                   — разрешить отправку тела
//   connection.setRequestProperty("Content-Type", "application/json") — заголовок
//   connection.outputStream.write(json.toByteArray())                 — записать тело
//   connection.responseCode                      — получить статус-код
//   connection.inputStream.bufferedReader().readText()  — прочитать тело ответа
//   connection.errorStream                       — поток ошибок (при коде 4xx/5xx)
//   connection.disconnect()                      — закрыть соединение


fun zapros(metod:String,url:String)  {
    val getUrl = URL("https://jsonplaceholder.typicode.com$url")  // ✅ template used
    println("getUrl ==== $getUrl")
    val getConn = getUrl.openConnection() as HttpURLConnection
    try{

        getConn.requestMethod = metod
        println("Metod ==== $metod")

        println("Код: ${getConn.responseCode}")
        val getBody = getConn.inputStream.bufferedReader().readText()
        println("Тело: $getBody")

    } catch(e:FileNotFoundException)
    {
        println("ЧТО ЗЫ ССЫЛКУ ТЫ МНЕ ДАЛ || ОНА НЕ РАБОАТЕТ// ВОТ ОШИБКА:")
        println(e.message)
    }catch (e: IOException) {
        println("Ошибка ввода-вывода (нет сети): ${e.message}")
    }finally {
        getConn.disconnect()
    }
}

fun main() {
    //disableSslVerification()

    // TODO 1: GET /posts/1
    // === GET запрос ===
    println("=== GET /posts/1 ===")
    val getUrl = URL("https://jsonplaceholder.typicode.com/posts/1")
    zapros("GET","/posts/1")



    // TODO 2: POST /posts
    println("\n=== POST /posts ===")
    zapros("POST","/posts")

    // TODO 3: GET /posts/9999 (несуществующий ресурс)
    println("\n=== GET /posts/9999 ===")
    zapros("GET","/posts/9999")
}