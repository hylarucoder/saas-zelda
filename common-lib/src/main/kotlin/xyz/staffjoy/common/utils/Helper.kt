package xyz.staffjoy.common.utils

object Helper {
    fun generateGravatarUrl(email: String?): String {
        val hash = MD5Util.md5Hex(email)
        return String.format("https://www.gravatar.com/avatar/%s.jpg?s=400&d=identicon", hash)
    }
}