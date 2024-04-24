package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.OutputEmitter
import ingsis.utils.Result
import ingsis.utils.getInputResult

class CompoundAssignationReadInputInterpreter(
    private val input: Input,
    private val scanners: List<ScanOperatorType>,
    private val output: OutputEmitter) :
    StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_INPUT
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as CompoundAssignationReadInput
        val declaration = statement.getDeclaration()
        val argument = ValueAnalyzer(scanners).analyze(statement.getArgument(), previousState)
        output.print(argument.getValue()!!)
        previousState[declaration.getVariable().getName()] =
            getInputResult(
                declaration.getType(),
                input.readInput(argument.getValue()!!),
                declaration.getKeyword().getModifier(),
            )
        return previousState
    }
}
