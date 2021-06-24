package xyz.zelda.web.mipha.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.stream.Collectors

open class ApiException : RuntimeException() {
    open val status = HttpStatus.BAD_REQUEST
    open val code = 200
    override val message = "OK"
}

class NotFoundException : ApiException() {
    override val status = HttpStatus.NOT_FOUND
    override val code = 404
    override val message = "NOT FOUND"
}

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {


    @ExceptionHandler(ApiException::class)
    fun handleApiException(
            ex: ApiException,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity(hashMapOf(
                "code" to ex.code,
                "message" to ex.message
        ), ex.status)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNodataFoundException(
            ex: NotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(hashMapOf(
                "code" to ex.code,
                "message" to ex.message
        ), ex.status)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        print(ex)
        return ResponseEntity(hashMapOf(
                "code" to "",
                "message" to ex.bindingResult.fieldErrors.stream().map { x -> x.defaultMessage }.collect(Collectors.toList()) as String
        ), HttpStatus.BAD_REQUEST)
    }
}