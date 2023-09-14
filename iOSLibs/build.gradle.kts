import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
val libraries = listOf("IOHKSecureRandomGeneration", "IOHKCryptoKit")
val sdks = listOf("iphoneos", "iphonesimulator", "macosx", "watchos", "watchsimulator", "appletvos", "appletvsimulator")

libraries.forEach { library ->
    sdks.forEach { sdk ->
        tasks.create<Exec>("build${library.capitalize()}${sdk.capitalize()}") {
            group = "build${library.capitalize()}"

            if (os.isMacOsX) {
                when (sdk) {
                    "iphoneos", "iphonesimulator" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Iphoneos",
                            "-sdk",
                            sdk
                        )
                    }
                    "macosx" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Macos",
                            "-sdk",
                            sdk
                        )
                    }
                    "watchos" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Watchos",
                            "-sdk",
                            sdk
                        )
                    }
                    "watchsimulator" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Watchos",
                            "-sdk",
                            sdk
                        )
                    }
                    "appletvos" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Tvos",
                            "-sdk",
                            sdk
                        )
                    }
                    "appletvsimulator" -> {
                        commandLine(
                            "xcodebuild",
                            "-project",
                            "$library/$library.xcodeproj",
                            "-target",
                            "${library}Tvos",
                            "-sdk",
                            sdk
                        )
                    }
                }
            } else {
                commandLine("echo", "Unsupported platform.")
            }

            workingDir(projectDir)

            inputs.files(
                fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
                fileTree("$projectDir/$library")
            )
            when (sdk) {
                "iphoneos" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-iphoneos")
                    )
                }
                "iphonesimulator" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-iphonesimulator")
                    )
                }
                "macosx" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release")
                    )
                }
                "watchos" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-watchos")
                    )
                }
                "watchsimulator" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-watchsimulator")
                    )
                }
                "appletvos" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-appletvos")
                    )
                }
                "appletvsimulator" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-appletvsimulator")
                    )
                }
            }
        }
    }
}

tasks.create<Delete>("clean") {
    group = "build"
    libraries.forEach {
        delete("$projectDir/$it/build")
    }
}
