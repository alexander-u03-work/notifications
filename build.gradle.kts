import nu.studer.gradle.jooq.JooqGenerate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType

plugins {
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("nu.studer.jooq") version "8.2"
    id("org.liquibase.gradle") version "2.2.2"
}

group = "ru.raggies"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val jdbcDriver = "org.postgresql.Driver"
val datasourceUrl = "jdbc:postgresql://localhost:5432/raggies"
val datasourceUsername = "postgres"
val datasourcePassword = "password"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    //implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    jooqGenerator("org.postgresql:postgresql")
    liquibaseRuntime("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("info.picocli:picocli:4.7.5")
    liquibaseRuntime("org.postgresql:postgresql:42.7.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:rabbitmq")
    //testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to "master.yaml",
            "url" to datasourceUrl,
            "username" to datasourceUsername,
            "password" to datasourcePassword,
            "driver" to jdbcDriver,
            "searchPath" to project.rootDir.absolutePath + "/src/main/resources/db/changelog/",
        )
    }
    runList = "main"
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = jdbcDriver
                    url = datasourceUrl
                    user = datasourceUsername
                    password = datasourcePassword
                    generator.apply {
                        name = "org.jooq.codegen.KotlinGenerator"
                        database.apply {
                            name = "org.jooq.meta.postgres.PostgresDatabase"
                            inputSchema = "notification_schema"
                            excludes = "DATABASECHANGELOG|DATABASECHANGELOGLOCK"
                            forcedTypes = listOf(ForcedType().apply {
                                name = "Instant"
                                includeTypes = "TIMESTAMP"
                            })
                        }
                        generate.apply {
                            isTables = true
                            isRecords = true
                            isDaos = true
                            isPojos = true
                            isPojosAsKotlinDataClasses = true
                            isGeneratedAnnotation = true
                            isSpringAnnotations = true
                        }
                        target.apply {
                            packageName = "ru.raggies.notification"
                        }
                        strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    }
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.withType<JooqGenerate>())
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
