package ingsis.sca.defaultConfig

import ingsis.sca.scan.cases.CaseScanner
import ingsis.sca.scan.cases.ScanCamelCase

fun getDefaultBooleanValue(name: String): Boolean {
    return if (name == "identifier") {
        true
    }
    else if (name == "expression") {
        true
    }
    else if (name == "literal") {
        true
    }
    else {
        false
    }
}

fun getDefaultCase(): ScanCamelCase {
    return ScanCamelCase()
}