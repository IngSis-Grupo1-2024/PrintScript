import components.Position
import components.Token
import components.ast.ASTInterface
import ingsis.parser.Parser
import ingsis.lexer.Lexer


//This may be an unnecessary class it can only be a
class Engine (val codelines:String){


    fun startEngine(){

    }


    //Parses each token list and returns a list that includes an AST for each code line.
    fun parse(): List<ASTInterface> {
        val parser = Parser()
        val treeList = ArrayList<ASTInterface>()

        for (tokenList in lexerTokenize()){
            treeList.add(parser.parse(tokenList))
        }
        return treeList
    }

    //Returns a list that includes a token list for each line
    fun lexerTokenize():List<List<Token>>{
        val tokenList = ArrayList<List<Token>>()
        val lexer = Lexer(Position())

        for (line in splitLines()){
            tokenList.add(lexer.tokenize(line))
        }
        return tokenList
    }


    //Splits the lines using the \n delimiter
    fun splitLines():List<String>{
        val codeLines: List<String> = codelines.split("\n")
        return codeLines
    }


}