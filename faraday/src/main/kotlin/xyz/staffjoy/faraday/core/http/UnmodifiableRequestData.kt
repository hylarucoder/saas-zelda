//package xyz.staffjoy.faraday.core.http
//
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpMethod
//import xyz.staffjoy.faraday.core.utils.BodyConverter
//import javax.servlet.http.HttpServletRequest
//
//open class UnmodifiableRequestData(var method: HttpMethod?,
//                                   var host: String,
//                                   var uri: String?,
//                                   var headers: HttpHeaders?,
//                                   var body: ByteArray?,
//                                   var originRequest: HttpServletRequest
//) {
//
//    constructor(requestData: RequestData) : this(
//            requestData.method,
//            requestData.host,
//            requestData.uri,
//            requestData.headers,
//            requestData.body,
//            requestData.originRequest
//    ) {
//    }
//
//    val bodyAsString: String
//        get() = BodyConverter.convertBodyToString(body)
//
//}