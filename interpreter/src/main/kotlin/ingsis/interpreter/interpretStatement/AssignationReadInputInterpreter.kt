package ingsis.interpreter.interpretStatement

import ingsis.components.statement.AssignationReadInput
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result
import ingsis.utils.checkIfNewValueTypeMatchesType
import ingsis.utils.checkMutability
import ingsis.utils.getInputResult

class AssignationReadInputInterpreter(private val input: Input, private val scanners: List<ScanOperatorType>) :
    StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.ASSIGNATION_READ_INPUT
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as AssignationReadInput
        val variable = statement.getVariable()
        if (checkMutability(variable, previousState)) {
            val variableType = previousState[variable.getName()]?.getType()
            val variableModifier = previousState[variable.getName()]?.getModifier()
            val argument = ValueAnalyzer(scanners).analyze(statement.getArgument(), previousState)
            val result = getInputResult(variableType!!, input.readInput(argument.getValue()!!), variableModifier!!)
            if (checkIfNewValueTypeMatchesType(variable, result, previousState)) {
                previousState[variable.getName()] = result
            } else {
                throw IllegalArgumentException("Variable ${variable.getName()} not declared")
            }
        } else {
            throw IllegalArgumentException("You can't update the value of an immutable variable")
        }
        return previousState
    }
}
