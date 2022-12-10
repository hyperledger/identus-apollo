import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

listOf("iphoneos", "iphonesimulator").forEach { sdk ->
    tasks.create<Exec>("build${sdk.capitalize()}") {
        group = "build"

        // Compile SwiftCryptoKit only on macOS.
        // IntelliJ isn't able to import project with this subproject on platform other than macOS
        if (os.isMacOsX) {
            commandLine(
                "xcodebuild",
                "-project",
                "SwiftCryptoKit.xcodeproj",
                "-target",
                "SwiftCryptoKit",
                "-sdk",
                sdk
            )
        } else {
            commandLine("echo", "Unsupported platform.")
        }

        workingDir(projectDir)

        inputs.files(
            fileTree("$projectDir/SwiftCryptoKit.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/SwiftCryptoKit")
        )

        outputs.files(
            fileTree("$projectDir/build/Release-$sdk")
        )
    }
}

tasks.create<Delete>("clean") {
    group = "build"
    delete("$projectDir/build")
}
