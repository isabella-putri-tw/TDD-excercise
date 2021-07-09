package lift

import lift.constants.Direction
import lift.exceptions.PassengerException
import lift.model.Lift
import lift.model.Passenger
import lift.model.Status
import kotlin.math.abs

class LiftApp(val lifts: List<Lift>) {

    fun tick() {
        for (lift in lifts) {
            lift.move()
        }
    }

    fun getAllLiftLocations(): String {
        return lifts.joinToString("|") { it.toString() }
    }

    fun getAllLiftPassengers(): String {
        return lifts.joinToString("|") { it.passengers.count().toString() }
    }

    fun call(level: Int, direction: Direction) {
        val assignedLift = lifts.minByOrNull { it -> abs(it.getAmountOfMovements(level, direction)) }
        assignedLift?.goTo(level)
    }

    fun enterPassenger(passengers: List<Passenger>, whichLift: Int) {
        if (lifts[whichLift].status == Status.OPENING) {
            lifts[whichLift].passengers.addAll(passengers)
            for (passenger in passengers) {
                passenger.enterLift(lifts[whichLift])
            }
        } else throw PassengerException("Passenger can't enter lift ${lifts[whichLift].name}, lift is at ${lifts[whichLift]}")
    }
}