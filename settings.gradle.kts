plugins {
    id("org.gradle.toolchains.foojay-resolver") version "0.4.0"
}

rootProject.name = "testsystem"

toolchainManagement {
    jvm {
        javaRepositories {
            repository("foojay") {
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}