package lift.model

import lift.constants.Direction
import java.util.*
import kotlin.math.abs

class Lift(var level: Int = 0, val name: String = "no_name") {
    val queue = LinkedList<Movement>()
    var status: Status = Status.IDLE
    val passengers = arrayListOf<Passenger>()

    companion object {
        val MAP_MOVEMENT_TO_STATUS = mapOf(
            Movement.OPEN to Status.OPENING,
            Movement.CLOSE to Status.CLOSING,
            Movement.GO_UP to Status.UP,
            Movement.GO_DOWN to Status.DOWN,
            Movement.DING to Status.DING
        )
        val MAP_DIRECTION_TO_MOVEMENT = mapOf(
            Direction.UP to Movement.GO_UP,
            Direction.DOWN to Movement.GO_DOWN
        )
    }

    fun move() {
        if (queue.isNotEmpty()) {
            val movement = queue.remove()
            status = MAP_MOVEMENT_TO_STATUS[movement]!!
            level = getFutureLevel(movement)
        } else if (status != Status.IDLE) {
            status = Status.IDLE
        }
    }

    fun getFutureLevel(movement: Movement, tempLevel: Int = level): Int {
        if (movement == Movement.GO_UP) return tempLevel + 1
        else if (movement == Movement.GO_DOWN) return tempLevel - 1
        return tempLevel
    }

    fun getAmountOfMovements(targetLevel: Int, direction: Direction): Int{
        var movements = 0
        var tempLevel: Int = level
        if (queue.isNotEmpty()) {
            for (i in 0 until queue.size) {
                tempLevel = getFutureLevel(queue[i], tempLevel)
                movements++
                if (tempLevel == targetLevel && MAP_DIRECTION_TO_MOVEMENT[direction] == queue[i]) {
                    return movements
                }
            }
        }
        while(targetLevel != tempLevel) {
            if (targetLevel > tempLevel) tempLevel = getFutureLevel(Movement.GO_UP, tempLevel)
            if (targetLevel < tempLevel) tempLevel = getFutureLevel(Movement.GO_DOWN, tempLevel)
            movements++
        }
        return movements
    }

    fun instructOpenClose(index: Int) {
        queue.add(index, Movement.CLOSE)
        queue.add(index, Movement.OPEN)
        queue.add(index, Movement.DING)
    }

    fun instructOpenClose() {
        queue.add(Movement.OPEN)
        queue.add(Movement.CLOSE)
    }

    fun goTo(targetLevel: Int) {
        val distance = getDistance(targetLevel)
        val movement = getMovement(targetLevel)

        if (queue.isNotEmpty()) {
            val futureLastDestination = stopOnTheWayIfPass(targetLevel)
            if (targetLevel != futureLastDestination) {
                goToAndOpen(getDistance(targetLevel, futureLastDestination), movement)
            }
            return
        }

        goToAndOpen(distance, movement)
    }

    private fun getMovement(targetLevel: Int) =
        if (targetLevel > level) Movement.GO_UP else Movement.GO_DOWN

    private fun getDistance(targetLevel: Int) = abs(targetLevel - level)
    private fun getDistance(targetLevel: Int, fromLevel: Int) = abs(targetLevel - fromLevel)

    private fun stopOnTheWayIfPass(targetLevel: Int): Int {
        var tempLevel: Int = level

        for (i in 0 until queue.size) {
            tempLevel = getFutureLevel(queue[i], tempLevel)

            if (tempLevel == targetLevel) {
                if (queue[i + 1] != Movement.DING)
                    instructOpenClose(i + 1)
                break
            }
        }
        return tempLevel
    }

    private fun goToAndOpen(distance: Int, movement: Movement) {
        if (distance > 0) {
            for (i in 1..distance) {
                queue.add(movement)
            }
            queue.add(Movement.DING)
        }

        instructOpenClose()
    }

    override fun toString(): String {
        if (status == Status.DING)
            return status.toString()
        if (status == Status.IDLE)
            return level.toString()
        return "$level $status"
    }
}