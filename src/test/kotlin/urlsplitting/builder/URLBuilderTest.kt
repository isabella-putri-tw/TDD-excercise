package urlsplitting.builder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import urlsplitting.exceptions.InvalidURLException
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class URLBuilderTest {

    @Nested
    inner class BuildFromString() {
        @Test
        fun `should throw exception when url format is invalid`() {
            val exception = assertFailsWith<InvalidURLException>(
                block = { URLBuilder.buildFromString("invalid_url") }
            )
            assertEquals("Invalid URL, must have '{protocol}://{domain name}/{path (optional)}' format, e.g. 'http://www.dummy.co/home'", exception.message)
        }

        @Test
        fun `should return URL without path when URL string is without path`() {
            val url = URLBuilder.buildFromString("http://www.bestcompany.co")
            assertEquals("http", url.protocol)
            assertEquals("www.bestcompany.co", url.domain)
            assertNull(url.path)
        }

        @Test
        fun `should return URL with path when URL string is with path`() {
            val url = URLBuilder.buildFromString("https://ww.notsogood.co/home/1")
            assertEquals("https", url.protocol)
            assertEquals("ww.notsogood.co", url.domain)
            assertEquals("home/1", url.path)
        }
    }

}