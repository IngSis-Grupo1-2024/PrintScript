package modules.engine


//This may be an unnecessary class it can only be a
class Main (val codelines:String){


    fun startEngine(){//The different calls to the functions must be here.

    }


    fun splitLines():List<String>{
        val codeLines: List<String> = codelines.split("\n")
        return codeLines
    }

//    fun lexerTokenize():List<Token>{
//        val lines = splitLines();
//        for (line in lines){
//        }
//    }



//    fun parser():AST{
//      this function calls the lexerTokenize and then returns the AST
//    }


}