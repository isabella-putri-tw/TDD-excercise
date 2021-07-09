package gameoflife

import gameoflife.model.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GameOfLifeTest {

    @Test
    fun playShouldDiesWhenDeadCellHaveAllDeadNeighbour() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.DEAD),
            CellsNeighbours(
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD)
            )
        )

        assertEquals(CellsAfterCondition.DIES, cellsCondition)
    }

    @Test
    fun playShouldDiesWhenALivingCellHaveLessThan2LivingNeighbours() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.ALIVE),
            CellsNeighbours(
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.DEAD)
            )
        )

        assertEquals(CellsAfterCondition.DIES, cellsCondition)
    }

    @Test
    fun playShouldSurviveWhenALivingCellHave2Or3LivingNeighbours() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.ALIVE),
            CellsNeighbours(
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.ALIVE)
            )
        )

        assertEquals(CellsAfterCondition.SURVIVES, cellsCondition)
    }

    @Test
    fun playShouldDiesWhenALivingCellHaveMoreThan3LivingNeighbours() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.ALIVE),
            CellsNeighbours(
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.UNKNOWN)
            )
        )

        assertEquals(CellsAfterCondition.DIES, cellsCondition)
    }

    @Test
    fun playShouldBecomeAliveWhenADeadCellHave3ExactlyAliveNeighbours() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.DEAD),
            CellsNeighbours(
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.ALIVE)
            )
        )

        assertEquals(CellsAfterCondition.BECOMES_ALIVE, cellsCondition)
    }

    @Test
    fun playShouldUnknownWhenALivingCellHaveAPossibilityMoreThan3LivingNeighbours() {
        val cellsCondition = GameOfLife.play(Cell(CellsInitCondition.ALIVE),
            CellsNeighbours(
                Cell(CellsInitCondition.ALIVE),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.DEAD),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.UNKNOWN),
                Cell(CellsInitCondition.ALIVE)
            )
        )

        assertEquals(CellsAfterCondition.UNKNOWN, cellsCondition)
    }
}