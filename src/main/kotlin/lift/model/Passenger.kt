package lift.model

import lift.exceptions.PassengerException

class Passenger {
    var lift: Lift? = null

    fun enterLift(lift: Lift) {
        this.lift = lift
    }

    fun exitLift() {
        if (lift != null && lift!!.status == Status.OPENING) {
            lift!!.passengers.remove(this)
            lift = null
        } else throw PassengerException("Passenger can't exit lift ${lift?.name}, lift is ${lift?.status}")
    }

    fun pressDestination(destination: Int) {
        lift?.goTo(destination)
    }
}