plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.serialization)
    alias(libs.plugins.api.doc)
    alias(libs.plugins.ktor)
}

group = "ch.abbts"
version = "0.1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // application dependencies
    implementation(libs.guava)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)
    implementation(libs.dbDriver)
    implementation(libs.bcrypt)

    // utility dependencies
    implementation(libs.config)
    implementation(libs.logging)
    implementation("io.ktor:ktor-server-cors:3.1.3")

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.ktor:ktor-server-cors:2.3.0")
    implementation("org.mindrot:jbcrypt:0.4")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

swagger {
    documentation {
        generateRequestSchemas = true
        hideTransientFields = true
        hidePrivateAndInternalFields = true
        deriveFieldRequirementFromTypeNullability = true
        info {
            title = "Find a Helper"
            description = "Docs for the backend API's of \"Find a Helper\""
            version = "10"
            contact {
                name = "Flavio Reber"
                url = "https://github.com/reberfla"
            }
        }
    }

    pluginOptions {
        format = "yaml" // or json
    }
}

application {
    mainClass = "ch.abbts.MainKt"
}

ktor {
    fatJar {
        archiveFileName.set("server.jar")
    }
}

tasks.named("distTar") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("shadowJar") {
    dependsOn(tasks.named("distZip"))
    dependsOn(tasks.named("startScripts"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
