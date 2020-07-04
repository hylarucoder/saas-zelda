package xyz.zelda.web.daruk.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import xyz.zelda.infra.api.ResultCode

@RestController
@RequestMapping("/api/article")
class ArticleController() {

    @GetMapping("/")
    fun findAll(): String {
        return "api/article"
    }

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) = slug

}

@RestController
@RequestMapping("/api/user")
class UserController() {

    @GetMapping("/")
    fun findAll(): String {
        return "api/article"
    }

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) = login
}