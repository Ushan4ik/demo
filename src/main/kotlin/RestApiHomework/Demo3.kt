package RestApiHomework

import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

val API_URL = "https://httpbin.org/bearer"

fun sendRequestWithAuth(token: String?) {
    try {
        val connection = URL(API_URL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer $token")
        }

        val responseCode = connection.responseCode
        println("Response Code: $responseCode")

        // Читаем тело ответа (обрабатываем возможный null в errorStream)
        val response = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error body"
        }

        println("Response Body: $response")
        connection.disconnect()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun main() {
    val encoder = Base64.getUrlEncoder().withoutPadding()
    val decoder = Base64.getUrlDecoder()

    // ========== TODO 1: Сборка JWT ==========
    println("=== Сборка JWT ===")
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val payload = """{"sub":"1","name":"Ivan Petrov","role":"student","iat":1234567890}"""
    val fakeSignature = "dummysignature"

    val headerEncoded = encoder.encodeToString(header.toByteArray())
    val payloadEncoded = encoder.encodeToString(payload.toByteArray())
    val signatureEncoded = encoder.encodeToString(fakeSignature.toByteArray())

    val token = "$headerEncoded.$payloadEncoded.$signatureEncoded"
    println("Assembled JWT: $token")

    // ========== TODO 2: Декодирование JWT ==========
    println("\n=== Декодирование JWT ===")
    val parts = token.split(".")
    if (parts.size == 3) {
        val decodedHeader = String(decoder.decode(parts[0]))
        val decodedPayload = String(decoder.decode(parts[1]))
        val decodedSignature = String(decoder.decode(parts[2]))

        println("Header: $decodedHeader")
        println("Payload: $decodedPayload")
        println("Signature: $decodedSignature")
    }

    // ========== TODO 3: GET /bearer с токеном ==========
    println("\n=== GET /bearer (с токеном) ===")
    sendRequestWithAuth(token)

    // ========== TODO 4: GET /bearer без токена ==========
    println("\n=== GET /bearer (без токена) ===")
    sendRequestWithAuth(null)

    // ========== TODO 5: Подмена payload ==========
    println("\n=== Подмена payload ===")
    // Изменяем роль на admin
    val modifiedPayload = """{"sub":"1","name":"Ivan Petrov","role":"admin","iat":1234567890}"""
    val modifiedPayloadEncoded = encoder.encodeToString(modifiedPayload.toByteArray())
    val tamperedToken = "$headerEncoded.$modifiedPayloadEncoded.$signatureEncoded"
    println("Tampered JWT: $tamperedToken")
    sendRequestWithAuth(tamperedToken)
}