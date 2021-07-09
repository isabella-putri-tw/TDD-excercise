package gameoflife

import gameoflife.model.Cell
import gameoflife.model.CellsAfterCondition
import gameoflife.model.CellsInitCondition
import gameoflife.model.CellsNeighbours

class GameOfLife {
    companion object {
        private val REQUIRED_LIVING_NEIGHBOURS = listOf(2, 3)
        private val LIVING_NEIGHBOURS_TO_BECOME_ALIVE = 3
        fun play(cell: Cell, neighbours: CellsNeighbours): CellsAfterCondition {
            if (cell.condition.equals(CellsInitCondition.DEAD) && neighbours.countAliveNeighbours() == LIVING_NEIGHBOURS_TO_BECOME_ALIVE)
                return CellsAfterCondition.BECOMES_ALIVE
            if (REQUIRED_LIVING_NEIGHBOURS.contains(neighbours.countAliveNeighbours())) {
                if (REQUIRED_LIVING_NEIGHBOURS.contains(neighbours.countAliveAndUnknownNeighbours()))
                    return CellsAfterCondition.SURVIVES
                return CellsAfterCondition.UNKNOWN
            }

            return CellsAfterCondition.DIES
        }
    }
}