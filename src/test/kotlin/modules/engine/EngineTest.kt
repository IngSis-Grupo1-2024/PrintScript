package modules.engine

import modules.engine.Main
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class EngineTest {
    @Test
    fun testSplitLines(){
        val lines = "let x:int = 4;\nlet y:int = x;"
        val main = Main(lines)
        val list = main.splitLines()
        assertEquals(2, list.size)
        assertEquals("let x:int = 4;", list[0])
        assertEquals("let y:int = x;", list[1])
    }

}