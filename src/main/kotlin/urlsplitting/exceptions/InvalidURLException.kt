package urlsplitting.exceptions

import java.util.*

class InvalidURLException(override val message: String): InputMismatchException(message) {
}