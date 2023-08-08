import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
val libraries = listOf("IOHKSecureRandomGeneration", "IOHKCryptoKit")
val sdks = listOf("iphoneos", "iphonesimulator", "macosx")

libraries.forEach { library ->
    sdks.forEach { sdk ->
        tasks.create<Exec>("build${library.capitalize()}${sdk.capitalize()}") {
            group = "build"

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
                            sdk,
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
                            sdk,
                        )
                    }
                }
            } else {
                commandLine("echo", "Unsupported platform.")
            }

            workingDir(projectDir)

            inputs.files(
                fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
                fileTree("$projectDir/$library"),
            )
            when (sdk) {
                "iphoneos" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-iphoneos"),
                    )
                }
                "iphonesimulator" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release-iphonesimulator"),
                    )
                }
                "macosx" -> {
                    outputs.files(
                        fileTree("$projectDir/$library/build/Release"),
                    )
                }
            }
        }
    }
//
//    tasks.create<Exec>("build${library.capitalize()}IosX64") {
//        group = "build"
//
//        if (os.isMacOsX) {
//            commandLine(
//                "xcodebuild",
//                "-project",
//                "$library/$library.xcodeproj",
//                "-target",
//                "$library",
//                "-sdk",
//                "iphonesimulator",
// //                "-arch",
// //                "x86_64",
// //                "CONFIGURATION_BUILD_DIR=$projectDir/$library/build/x86_64-iphonesimulator",
//            )
//        } else {
//            commandLine("echo", "Unsupported platform.")
//        }
//
//        workingDir(projectDir)
//
//        inputs.files(
//            fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
//            fileTree("$projectDir/$library"),
//        )
//
//        outputs.files(
//            fileTree("$projectDir/$library/build/x86_64-iphonesimulator"),
//        )
//    }
//
//    tasks.create<Exec>("build${library.capitalize()}IosSimulatorArm64") {
//        group = "build"
//
//        if (os.isMacOsX) {
//            commandLine(
//                "xcodebuild",
//                "-project",
//                "$library/$library.xcodeproj",
//                "-target",
//                "$library",
//                "-sdk",
//                "iphonesimulator",
// //                "-arch",
// //                "arm64",
// //                "CONFIGURATION_BUILD_DIR=$projectDir/$library/build/arm64-iphonesimulator",
//            )
//        } else {
//            commandLine("echo", "Unsupported platform.")
//        }
//
//        workingDir(projectDir)
//
//        inputs.files(
//            fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
//            fileTree("$projectDir/$library"),
//        )
//
//        outputs.files(
//            fileTree("$projectDir/$library/build/arm64-iphonesimulator"),
//        )
//    }
//
//    tasks.create<Exec>("build${library.capitalize()}MacosX64") {
//        group = "build"
//
//        if (os.isMacOsX) {
//            commandLine(
//                "xcodebuild",
//                "-project",
//                "$library/$library.xcodeproj",
//                "-target",
//                "${library}Macos",
//                "-sdk",
//                "macosx",
// //                "-arch",
// //                "x86_64",
//                "CONFIGURATION_BUILD_DIR=$projectDir/$library/build/x86_64-macos",
//            )
//        } else {
//            commandLine("echo", "Unsupported platform.")
//        }
//
//        workingDir(projectDir)
//
//        inputs.files(
//            fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
//            fileTree("$projectDir/$library"),
//        )
//
//        outputs.files(
//            fileTree("$projectDir/$library/build/x86_64-macos"),
//        )
//    }
//
//    tasks.create<Exec>("build${library.capitalize()}MacosArm64") {
//        group = "build"
//
//        if (os.isMacOsX) {
//            commandLine(
//                "xcodebuild",
//                "-project",
//                "$library/$library.xcodeproj",
//                "-target",
//                "${library}Macos",
//                "-sdk",
//                "macosx",
//                "-arch",
//                "arm64",
//                "CONFIGURATION_BUILD_DIR=$projectDir/$library/build/arm64-macos",
//            )
//        } else {
//            commandLine("echo", "Unsupported platform.")
//        }
//
//        workingDir(projectDir)
//
//        inputs.files(
//            fileTree("$projectDir/$library.xcodeproj") { exclude("**/xcuserdata") },
//            fileTree("$projectDir/$library"),
//        )
//
//        outputs.files(
//            fileTree("$projectDir/$library/build/arm64-macos"),
//        )
//    }
}

tasks.create<Delete>("clean") {
    group = "build"
    libraries.forEach {
        delete("$projectDir/$it/build")
    }
}
