import org.gradle.internal.os.OperatingSystem

val currentOs: OperatingSystem = OperatingSystem.current()
val bash = "bash"

val buildSecp256k1 by tasks.creating { group = "build" }

val buildSecp256k1Ios by tasks.creating(Exec::class) {
    group = "build"
    buildSecp256k1.dependsOn(this)

    onlyIf { currentOs.isMacOsX }

    inputs.files(projectDir.resolve("build-ios.sh"))
    outputs.dir(projectDir.resolve("build/ios"))

    workingDir = projectDir
    commandLine(bash, "build-ios.sh")
}

val clean by tasks.creating {
    group = "build"

    doLast {
        delete(projectDir.resolve("build"))
    }
}
