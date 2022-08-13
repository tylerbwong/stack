package me.tylerbwong.stack.data.site

/**
 * In order to extract the correct site parameter to pass around, all non Stack Exchange sites
 * need to drop ".stackexchange.com" and all others need to drop ".com". This is a rudimentary
 * solution and will need to be updated if any site is added that does not end in ".com".
 */
fun String.normalizeSite(): String {
    return removePrefix("https://")
        .removeSuffix(".com")
        .removeSuffix(".stackexchange")
}
