package modules.parser

import components.ASTInterface
import components.Token

interface ParserInterface {
    fun parse(tokens: List<Token>): ASTInterface
}