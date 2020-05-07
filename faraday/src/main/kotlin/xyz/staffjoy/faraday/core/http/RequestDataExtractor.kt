//package xyz.staffjoy.faraday.core.http
//
//import org.apache.commons.io.IOUtils.toByteArray
//import org.apache.commons.lang3.StringUtils.EMPTY
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpMethod
//import xyz.staffjoy.faraday.exceptions.FaradayException
//import java.io.IOException
//import java.util.*
//import javax.servlet.http.HttpServletRequest
//
//class RequestDataExtractor {
//    fun extractBody(request: HttpServletRequest): ByteArray {
//        return try {
//            toByteArray(request.inputStream)
//        } catch (e: IOException) {
//            throw FaradayException("Error extracting body of HTTP request with URI: " + extractUri(request), e)
//        }
//    }
//
//    fun extractHttpHeaders(request: HttpServletRequest): HttpHeaders {
//        val headers = HttpHeaders()
//        val headerNames = request.headerNames
//        while (headerNames.hasMoreElements()) {
//            val name = headerNames.nextElement()
//            val value: List<String> = Collections.list(request.getHeaders(name))
//            headers[name] = value
//        }
//        return headers
//    }
//
//    fun extractHttpMethod(request: HttpServletRequest): HttpMethod? {
//        return HttpMethod.resolve(request.method)
//    }
//
//    fun extractUri(request: HttpServletRequest): String {
//        return request.requestURI + getQuery(request)
//    }
//
//    fun extractHost(request: HttpServletRequest): String {
//        return request.serverName
//    }
//
//    protected fun getQuery(request: HttpServletRequest): String {
//        return if (request.queryString == null) EMPTY else "?" + request.queryString
//    }
//}