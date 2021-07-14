package lift

import lift.constants.Direction
import lift.exceptions.PassengerException
import lift.model.Lift
import lift.model.Passenger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class LiftAppTest {
    @Test
    fun `lifts should be at level 0 in the beginning`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        assertEquals("0|0", liftApp.getAllLiftLocations())
    }

    @Test
    fun `lift should open after a call from the same floor`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        liftApp.call(0, Direction.UP)
        assertEquals("0|0", liftApp.getAllLiftLocations())
        liftApp.tick()
        assertEquals("0 OPENING|0", liftApp.getAllLiftLocations())
    }

    @Test
    fun `lift should close after opened`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        liftApp.call(0, Direction.UP)
        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("0 CLOSING|0", liftApp.getAllLiftLocations())
    }

    @Test
    fun `lift should be idle after closed`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        liftApp.call(0, Direction.UP)
        for (i in 1..3) {
            liftApp.tick()
        }

        assertEquals("0|0", liftApp.getAllLiftLocations())
    }


    @Test
    fun `passenger can't enter lift when lift is not opened`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)

        val exception = assertFailsWith<PassengerException>(
            block = { liftApp.enterPassenger(listOf(passenger), 0) }
        )
        assertEquals("Passenger can't enter lift A, lift is at 0", exception.message)
        assertEquals("0|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `passenger can enter lift when lift is opening`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)

        assertEquals("0 OPENING|0", liftApp.getAllLiftLocations())
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `lift should go up when passenger press upper level`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("1 UP|0", liftApp.getAllLiftLocations())
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `lift should be ding when arrived`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..3) {
            liftApp.tick()
        }

        assertEquals("DING|0", liftApp.getAllLiftLocations())
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `lift should be opening after arrived`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..4) {
            liftApp.tick()
        }

        assertEquals("1 OPENING|0", liftApp.getAllLiftLocations())
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `passenger can exit lift when lift is opening`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..4) {
            liftApp.tick()
        }
        passenger.exitLift()

        assertEquals("1 OPENING|0", liftApp.getAllLiftLocations())
        assertEquals("0|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `passenger can't exit lift when lift isn't open`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..5) {
            liftApp.tick()
        }
        val exception = assertFailsWith<PassengerException>(
            block = { passenger.exitLift() }
        )
        assertEquals("Passenger can't exit lift A, lift is CLOSING", exception.message)
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `lift should be idle after closing`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..4) {
            liftApp.tick()
        }
        passenger.exitLift()
        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("1|0", liftApp.getAllLiftLocations())
        assertEquals("0|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun `first lift should go to level 2 when called`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(2)
        for (i in 1..4) {
            liftApp.tick()
        }

        assertEquals("DING|0", liftApp.getAllLiftLocations())
    }

    @Test
    fun `second lift should go to level 2 when lift is nearer to called level`() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(1, "B")))
        val passenger = Passenger()

        liftApp.call(2, Direction.UP)
        for (i in 1..3) {
            liftApp.tick()
        }
        liftApp.enterPassenger(listOf(passenger), 1)
        passenger.pressDestination(5)

        assertEquals("0|2 OPENING", liftApp.getAllLiftLocations())
        assertEquals("0|1", liftApp.getAllLiftPassengers())

        for (i in 1..6) {
            liftApp.tick()
        }
        passenger.exitLift()

        assertEquals("0|5 OPENING", liftApp.getAllLiftLocations())
        assertEquals("0|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun liftShouldMoveDownWhenLiftIsOpeningAndDirectionIsUp() {
        val liftApp = LiftApp(listOf(Lift(0, "A"), Lift(0, "B")))
        val passenger = Passenger()

        liftApp.call(5, Direction.DOWN)

        for (i in 1..7) {
            liftApp.tick()
        }

        liftApp.enterPassenger(listOf(passenger), 0)
        passenger.pressDestination(1)
        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("4 DOWN|0", liftApp.getAllLiftLocations())
        assertEquals("1|0", liftApp.getAllLiftPassengers())
    }

    @Test
    fun otherLiftShouldPickUpPassengerWhenOtherLiftAlreadyCalled() {
        val liftApp = LiftApp(listOf(Lift(2, "A"), Lift(0, "B")))
        val passenger1 = Passenger()
        val passenger2 = Passenger()

        liftApp.call(0, Direction.UP)
        liftApp.call(2, Direction.DOWN)

        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger1), 1)
        liftApp.enterPassenger(listOf(passenger2), 0)

        assertEquals("1|1", liftApp.getAllLiftPassengers())
    }

    @Test
    fun occupiedLiftShouldPickUpPassengerWhenLiftHaveIsNearer() {
        val liftApp = LiftApp(listOf(Lift(2, "A"), Lift(0, "B")))
        val passenger1 = Passenger()
        val passenger2 = Passenger()

        liftApp.call(2, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger1), 0)
        passenger1.pressDestination(5)
        liftApp.call(3, Direction.UP)
        for (i in 1..4) {
            liftApp.tick()
        }
        liftApp.enterPassenger(listOf(passenger2), 0)

        assertEquals("2|0", liftApp.getAllLiftPassengers())
    }


    @Test
    fun `lift should deliver one time when 2 different passenger go to the same floor`() {
        val liftApp = LiftApp(listOf(Lift(2, "A"), Lift(0, "B")))
        val passenger1 = Passenger()
        val passenger2 = Passenger()

        liftApp.call(2, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger1), 0)
        passenger1.pressDestination(5)
        liftApp.call(3, Direction.UP)
        for (i in 1..4) {
            liftApp.tick()
        }
        liftApp.enterPassenger(listOf(passenger2), 0)
        passenger2.pressDestination(5)
        for (i in 1..5) {
            liftApp.tick()
        }

        assertEquals("5 OPENING|0", liftApp.getAllLiftLocations())

        passenger1.exitLift()
        passenger2.exitLift()
        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("5|0", liftApp.getAllLiftLocations())
    }

    @Test
    fun `lift should deliver two time when 2 different passenger go to the same direction`() {
        val liftApp = LiftApp(listOf(Lift(2, "A"), Lift(0, "B")))
        val passenger1 = Passenger()
        val passenger2 = Passenger()

        liftApp.call(2, Direction.UP)
        liftApp.tick()
        liftApp.enterPassenger(listOf(passenger1), 0)
        passenger1.pressDestination(5)
        liftApp.call(3, Direction.UP)
        for (i in 1..4) {
            liftApp.tick()
        }
        liftApp.enterPassenger(listOf(passenger2), 0)
        passenger2.pressDestination(7)
        for (i in 1..5) {
            liftApp.tick()
        }

        assertEquals("5 OPENING|0", liftApp.getAllLiftLocations())
        passenger1.exitLift()

        for (i in 1..5) {
            liftApp.tick()
        }

        assertEquals("7 OPENING|0", liftApp.getAllLiftLocations())
        passenger2.exitLift()

        for (i in 1..2) {
            liftApp.tick()
        }
        assertEquals("7|0", liftApp.getAllLiftLocations())
    }


    @Test
    fun `different lift should deliver when 2 different passenger go to different direction`() {
        /*
        | A | B |
        | 2 | 0 |
        */
        val liftApp = LiftApp(listOf(Lift(2, "A"), Lift(0, "B")))
        val passenger1 = Passenger()
        val passenger2 = Passenger()

        liftApp.call(2, Direction.UP)
        /*
        | A         | B |
        | 2 OPENING | 0 |
         */
        liftApp.tick()

        liftApp.enterPassenger(listOf(passenger1), 0)
        passenger1.pressDestination(5)
        liftApp.call(3, Direction.DOWN)
        for (i in 1..5) {
            /*
            | A         | B         |
            | 2 CLOSING | 1 UP      |
            | 3 UP      | 2 UP      |
            | 4 UP      | 3 UP      |
            | 5 UP      | DING      |
            | DING      | 3 OPENING |
            */
            liftApp.tick()
        }
        assertEquals("DING|3 OPENING", liftApp.getAllLiftLocations())

        liftApp.enterPassenger(listOf(passenger2), 1)
        passenger2.pressDestination(1)

        /*
        | A         | B         |
        | 5 OPENING | 3 CLOSING |
        */
        liftApp.tick()
        passenger1.exitLift()

        for (i in 1..4) {
            /*
            | A         | B         |
            | 5 CLOSING | 2 DOWN    |
            | 5         | 1 DOWN    |
            | 5         | DING      |
            | 5         | 1 OPENING |
            */
            liftApp.tick()
        }
        assertEquals("5|1 OPENING", liftApp.getAllLiftLocations())
        passenger2.exitLift()

        for (i in 1..2) {
            liftApp.tick()
        }

        assertEquals("5|1", liftApp.getAllLiftLocations())
        assertEquals("0|0", liftApp.getAllLiftPassengers())
    }
}