package RestApiHomework

import java.net.HttpURLConnection
import java.net.URL

// ===========================================
// Задача 2. REST — полный CRUD
// ===========================================
// Цель: реализовать все CRUD-операции для ресурса /posts.
// API: https://jsonplaceholder.typicode.com/posts
//
// TODO 1: Реализовать sendRequest() — универсальную функцию отправки запросов
// TODO 2: Реализовать 5 CRUD-функций (ниже)
// TODO 3: Вызвать каждую функцию в main() и вывести результат
//
// Вопросы после выполнения:
//   - В чём разница между PUT и PATCH?
//   - Почему POST возвращает 201, а PUT возвращает 200?
//   - Какой метод идемпотентный, а какой нет?

val BASE_URL = "https://jsonplaceholder.typicode.com/posts"

/** Универсальная функция для отправки HTTP-запросов.
 *  @param urlStr  — полный URL
 *  @param method  — HTTP-метод (GET, POST, PUT, DELETE)
 *  @param body    — тело запроса в формате JSON (null для GET/DELETE)
 *  @return Pair(statusCode, responseBody)
 */
fun sendRequest(urlStr: String, method: String, body: String? = null): Pair<Int, String> {
    val connection = URL(urlStr).openConnection() as HttpURLConnection
    connection.requestMethod = method
    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.outputStream.write(body.toByteArray())
    }
    val code = connection.responseCode
    val response = (if (code in 200..299) connection.inputStream else connection.errorStream)
        ?.bufferedReader()?.readText() ?: ""
    connection.disconnect()
    return code to response
}

/** GET /posts — получить все посты */
fun getPosts(): String {
    println("""
        BRUH GET POSTS STARTING
    """.trimIndent())
    for(i in 98..101)
    {
        println(getPost(i))
    }
    return "everything is ok"
}

/** GET /posts/{id} — получить пост по ID */
fun getPost(id: Int): String {

    val answer = sendRequest("$BASE_URL/$id", "GET")
    if (answer.first in 200..299){
        println("Ok, posts/$id Answer is " + answer.first)
        println(answer.second)
        println()
        return answer.second
    }
    else
    {
        return "Crashed on post $id error ${answer.first}"
    }
}

/** POST /posts — создать новый пост. Тело: {"title":"...", "body":"...", "userId":1} */
/** POST /posts — создать новый пост. Тело: {"title":"...", "body":"...", "userId":1} */
fun createPost(json: String): String {
    return try {
        println("Parsing input: '$json'")

        val bodyIndex = json.indexOf("body:")
        val titleIndex = json.indexOf("title:")
        val bodyStart = bodyIndex + 5
        val titleStart = titleIndex + 6

        val body = json.substring(bodyStart, titleIndex).trim()
        println("Extracted body: '$body'")
        val title = json.substring(titleStart).trim()
        println("Extracted title: '$title'")

        val postData = """{"title":"$title","body":"$body","userId":1}"""

        val (code, response) = sendRequest(BASE_URL, "POST", postData)
        "Status: $code, Response: $response"
    } catch (e: Exception) {
        "Error creating post: ${e.message}"
    } as String
}


/** PUT /posts/{id} — полностью обновить пост */
fun updatePost(id: Int, json: String): String {
    return try {
        println("Parsing input: '$json'")

        val bodyIndex = json.indexOf("body:")
        val titleIndex = json.indexOf("title:")
        val bodyStart = bodyIndex + 5
        val titleStart = titleIndex + 6

        val body = json.substring(bodyStart, titleIndex).trim()
        println("Extracted body: '$body'")
        val title = json.substring(titleStart).trim()
        println("Extracted title: '$title'")

        val postData = """{"title":"$title","body":"$body","userId":1}"""

        val (code, response) = sendRequest("$BASE_URL/$id", "PUT", postData)
        "Status: $code, Response: $response"
    } catch (e: Exception) {
        "Error creating post: ${e.message}"
    } as String
}

/** DELETE /posts/{id} — удалить пост, вернуть статус-код */
fun deletePost(id: Int): Int {
    return sendRequest("$BASE_URL/$id", "DELETE").first
}

fun main() {
   // disableSslVerification()

    // TODO 3: вызвать каждую функцию и вывести результат
    println("=== GET ALL ===")
    val i = 1
    println("$BASE_URL$i")
    println(getPosts())
    println("\n=== GET ONE ===")
    println(getPost(4))
    println("\n=== CREATE ===")
    println(createPost("body: Baze123 title:BAZE_TITLE"))
    println("\n=== UPDATE ===")
    println(updatePost(11,"body: Baze123 title:BAZE_TITLE"))
    println("\n=== DELETE ===")
    println(deletePost(1))
}