package xyz.zelda.infra.exception

import com.github.structlog4j.SLoggerFactory
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.api.ResultCode
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionTranslator {
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleError(e: MissingServletRequestParameterException): BaseResponse {
        logger.warn("Missing Request Parameter", e)
        val message = String.format("Missing Request Parameter: %s", e.parameterName)
        return BaseResponse(
                ResultCode.PARAM_MISS,
                message
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleError(e: MethodArgumentTypeMismatchException): BaseResponse {
        logger.warn("Method Argument Type Mismatch", e)
        val message = String.format("Method Argument Type Mismatch: %s", e.name)
        return BaseResponse(
                ResultCode.PARAM_TYPE_ERROR,
                message
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleError(e: MethodArgumentNotValidException): BaseResponse {
        logger.warn("Method Argument Not Valid", e)
        val result = e.bindingResult
        val error = result.fieldError
        val message = String.format("%s:%s", error!!.field, error.defaultMessage)
        return BaseResponse(
                ResultCode.PARAM_VALID_ERROR,
                message
        )
    }

    @ExceptionHandler(BindException::class)
    fun handleError(e: BindException): BaseResponse {
        logger.warn("Bind Exception", e)
        val error = e.fieldError
        val message = String.format("%s:%s", error!!.field, error.defaultMessage)
        return BaseResponse(
                ResultCode.PARAM_BIND_ERROR,
                message
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleError(e: ConstraintViolationException): BaseResponse {
        logger.warn("Constraint Violation", e)
        val violations = e.constraintViolations
        val violation = violations.iterator().next()
        val path = (violation.propertyPath as PathImpl).leafNode.name
        val message = String.format("%s:%s", path, violation.message)
        return BaseResponse(
                ResultCode.PARAM_VALID_ERROR,
                message
        )
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleError(e: NoHandlerFoundException): BaseResponse {
        logger.error("404 Not Found", e)
        return BaseResponse(
                ResultCode.NOT_FOUND,
                e.message
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleError(e: HttpMessageNotReadableException): BaseResponse {
        logger.error("Message Not Readable", e)
        return BaseResponse(
                ResultCode.MSG_NOT_READABLE,
                e.message
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleError(e: HttpRequestMethodNotSupportedException): BaseResponse {
        logger.error("Request Method Not Supported", e)
        return BaseResponse(
                ResultCode.METHOD_NOT_SUPPORTED,
                e.message
        )
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleError(e: HttpMediaTypeNotSupportedException): BaseResponse {
        logger.error("Media Type Not Supported", e)
        return BaseResponse(
                ResultCode.MEDIA_TYPE_NOT_SUPPORTED,
                e.message
        )
    }

    @ExceptionHandler(ServiceException::class)
    fun handleError(e: ServiceException): BaseResponse {
        logger.error("Service Exception", e)
        return BaseResponse(
                e.status,
                e.message
        )
    }

    @ExceptionHandler(PermissionDeniedException::class)
    fun handleError(e: PermissionDeniedException): BaseResponse {
        logger.error("Permission Denied", e)
        return BaseResponse(
                e.status,
                e.message
        )
    }

    @ExceptionHandler(Throwable::class)
    fun handleError(e: Throwable): BaseResponse {
        logger.error("Internal Server Error", e)
//        return BaseResponse(
//                e.getResultCode(),
//                e.message
//        )
        return BaseResponse(
                ResultCode.REQ_REJECT, // TODO: polish
                e.message
        )
    }

    companion object {
        val logger = SLoggerFactory.getLogger(GlobalExceptionTranslator::class.java)
    }
}