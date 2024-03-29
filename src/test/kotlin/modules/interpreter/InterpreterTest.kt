package modules.interpreter

import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
    @Test
    fun `declaration of x as string`() {
        val interpreter = Interpreter()
        val position = Position()
        val ast: ASTInterface =
            AST(
                Token(position, ":", TokenType.ASSIGNATION),
                AST(Token(position, "x", TokenType.IDENTIFIER)),
                AST(Token(position, "string", TokenType.TYPE)),
            )
        val typeMapExpected = HashMap<String, String>()
        typeMapExpected.put("x", "string")
        assertEquals(
            typeMapExpected,
            interpreter.interpret(ast, ValueAndTypeMaps(HashMap<String, String>(), HashMap<String, String>())).typeMap,
        )
    }
}
