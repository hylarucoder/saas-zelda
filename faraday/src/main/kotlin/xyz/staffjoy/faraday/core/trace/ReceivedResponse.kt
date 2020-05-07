//package xyz.staffjoy.faraday.core.trace
//
//import org.springframework.http.HttpStatus
//import xyz.staffjoy.faraday.core.utils.BodyConverter
//
//class ReceivedResponse : HttpEntity() {
//    var status: HttpStatus? = null
//        protected set
//    var body: ByteArray
//        protected set
//
//    val bodyAsString: String
//        get() = BodyConverter.convertBodyToString(body)
//
//}