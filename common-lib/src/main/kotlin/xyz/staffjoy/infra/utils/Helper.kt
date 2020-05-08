package xyz.staffjoy.common.utils

object Helper {
    fun generateGravatarUrl(email: String?): String {
        val hash = email?.let { MD5Util.md5Hex(it) }
        return String.format("https://www.gravatar.com/avatar/%s.jpg?s=400&d=identicon", hash)
    }
}