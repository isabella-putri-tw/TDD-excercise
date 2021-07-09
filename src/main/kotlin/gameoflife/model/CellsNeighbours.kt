package gameoflife.model

class CellsNeighbours(
    neighbourEast: Cell,
    neighbourSouthEast: Cell,
    neighbourSouth: Cell,
    neighbourSouthWest: Cell,
    neighbourWest: Cell,
    neighbourNorthWest: Cell,
    neighbourNorth: Cell,
    neighbourNorthEast: Cell,
) {
    val neighbours: List<Cell> = listOf(
        neighbourEast,
        neighbourSouthEast,
        neighbourSouth,
        neighbourSouthWest,
        neighbourWest,
        neighbourNorthWest,
        neighbourNorth,
        neighbourNorthEast,
    )

    fun countAliveNeighbours(): Int {
        return neighbours.count { x -> x.condition == CellsInitCondition.ALIVE }
    }
    fun countAliveAndUnknownNeighbours(): Int {
        return neighbours.count {
            x -> x.condition == CellsInitCondition.ALIVE || x.condition == CellsInitCondition.UNKNOWN
        }
    }
}