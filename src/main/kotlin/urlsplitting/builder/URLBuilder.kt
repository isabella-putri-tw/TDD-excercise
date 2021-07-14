package urlsplitting.builder

import urlsplitting.exceptions.InvalidURLException
import urlsplitting.model.URL

class URLBuilder {
    companion object {
        @Throws(InvalidURLException::class)
        fun buildFromString(urlString: String): URL {
            if (!urlString.contains("://")) throw InvalidURLException("Invalid URL, must have '{protocol}://{domain name}/{path (optional)}' format, e.g. 'http://www.dummy.co/home'")
            val (protocol, domainPath) = urlString.split("://")
            if (domainPath.contains("/")){
                val splittedDomainPath = domainPath.split("/")
                val domain = splittedDomainPath[0]
                var path: String? = getPath(splittedDomainPath)
                return URL(protocol, domain, path)
            }
            return URL(protocol, domainPath)
        }

        private fun getPath(splittedDomainPath: List<String>): String? {
            var path: String? = null
            if (splittedDomainPath.size > 1) {
                path = splittedDomainPath.subList(1, splittedDomainPath.size).joinToString("/")
            }
            return path
        }
    }

}