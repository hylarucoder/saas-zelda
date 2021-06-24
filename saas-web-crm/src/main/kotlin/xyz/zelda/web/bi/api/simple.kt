package xyz.zelda.web.mipha.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import xyz.zelda.web.mipha.exception.NotFoundException
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


sealed class TestResult {
    class Int(val value: Int) : TestResult()
    class String(val value: String) : TestResult()
}

data class HelloResponse(
        val message: String,
        val name: String
)


@RestController
@RequestMapping("/api/simple")
class SimpleController {

    @GetMapping("/hello")
    @ResponseBody
    fun getHello(): HashMap<String, Int> = hashMapOf("1" to 2, "2" to 3)

    @GetMapping("/helloworld")
    fun getHelloWordMessage(): ResponseEntity<String> =
            ResponseEntity.ok("Hello World")

    @GetMapping("/helloworld/{name}")
    fun getHelloWordMessageWithName(
            @PathVariable("name") name: String
    ): ResponseEntity<Any> =
            if (name != "Cristian") {
                ResponseEntity.ok(
                        HelloResponse(
                                message = "Hello $name",
                                name = name
                        )
                )
            } else {
                ResponseEntity.badRequest().body("I am Cristian")
            }

    @GetMapping("/ex/404")
    fun getEx404(): String {
        throw NotFoundException()
    }
}

