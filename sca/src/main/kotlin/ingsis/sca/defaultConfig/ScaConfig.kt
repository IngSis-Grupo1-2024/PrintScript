package ingsis.sca.defaultConfig

import ingsis.sca.scan.cases.ScanCamelCase

fun getDefaultBooleanValue(name: String): Boolean {
    // You are free to add any new type if not specified in the rules file.
    return if (name == "identifier") {
        true
    } else if (name == "expression") {
        true
    } else if (name == "literal") {
        true
    } else {
        false
    }
}

fun getDefaultCase(): ScanCamelCase {
    return ScanCamelCase()
}
