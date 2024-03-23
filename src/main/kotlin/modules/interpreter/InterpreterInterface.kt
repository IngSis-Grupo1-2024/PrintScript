package modules.interpreter

import components.ast.AST
import components.ast.ASTInterface

interface InterpreterInterface {
    fun interpret(ast: ASTInterface, mapSet: ValueAndTypeMaps) : ValueAndTypeMaps
}