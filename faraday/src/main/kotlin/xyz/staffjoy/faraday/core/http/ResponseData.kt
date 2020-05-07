//package xyz.staffjoy.faraday.core.http
//
//import org.springframework.http.HttpHeaders
//import org.springframework.http.HttpStatus
//import xyz.staffjoy.faraday.core.utils.BodyConverter
//
//class ResponseData(var status: HttpStatus, headers: HttpHeaders?, body: ByteArray, requestData: UnmodifiableRequestData) {
//    var headers: HttpHeaders
//    var body: ByteArray
//    var requestData: UnmodifiableRequestData
//        protected set
//
//    val bodyAsString: String
//        get() = BodyConverter.convertBodyToString(body)
//
//    fun setBody(body: String?) {
//        this.body = BodyConverter.convertStringToBody(body)
//    }
//
//    init {
//        this.headers = HttpHeaders()
//        this.headers.putAll(headers!!)
//        this.body = body
//        this.requestData = requestData
//    }
//}