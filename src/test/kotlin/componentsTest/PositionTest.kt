package componentsTest

import components.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionTest {

    @Test
    fun testPositionCreation(){
        val position = Position(10,15,30,25,60)
        assertEquals(10, position.startOffset)
        assertEquals(0, position.endColumn)
    }

    @Test
    fun testPositionWithNoArguments(){
        val position = Position()
        assertEquals(0, position.startOffset)
        assertEquals(0, position.endColumn)
    }



}