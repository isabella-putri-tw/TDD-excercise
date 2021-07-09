package lift.exceptions

import java.util.*

class PassengerException(override val message: String): InputMismatchException(message) {
}