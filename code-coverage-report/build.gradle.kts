plugins {
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {

}


reporting {
    reports {
        val testCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
}

