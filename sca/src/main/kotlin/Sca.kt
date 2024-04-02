import components.ast.ASTInterface
import scaRules.Rule

class Sca(private val rules: ArrayList<Rule>) : ScaInterface {
    fun getRules(): ArrayList<Rule> {
        return rules
    }

    override fun analyze(ast: ASTInterface): Boolean {
        var result = false
        for (rule in rules) {
            result = result || rule.validate(ast)
        }
        return result
    }
}
