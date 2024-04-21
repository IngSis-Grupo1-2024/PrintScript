package ingsis.interpreter.environment

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.interpreter.interpretStatement.VariableType

class Environment (private val variables: Map<String, EnvironmentVariable> = emptyMap()){

    fun getVariable(name: String): EnvironmentVariable? {
        return try {
            variables[name]
        } catch (e: Exception) {
            throw Exception("Variable $name not found")
        }
    }

    fun createEnvironment(): Environment {
        val envVariables = System.getenv()
        val updatedVariables = variables.toMutableMap()

        envVariables.forEach { (name, value) ->
            val existingVariable = variables[name]
            if (existingVariable != null) {
                updatedVariables[name] = EnvironmentVariable(getVariableType(value), SingleValue(Token(Position(), value, TokenType.VALUE)))
            } else {
                updatedVariables[name] = EnvironmentVariable(getVariableType(value), SingleValue(Token(Position(), value, TokenType.VALUE)))
            }
        }
        return Environment(updatedVariables)
    }

    private fun getVariableType(value: String): VariableType {
        return when {
            value.toIntOrNull() != null -> VariableType.NUMBER
            value.toBoolean() != null -> VariableType.BOOLEAN
            else -> VariableType.STRING
        }
    }

}