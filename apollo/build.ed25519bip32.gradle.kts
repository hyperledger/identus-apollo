val ed25519bip32Dir = rootDir.resolve("rust-ed25519-bip32")
val taskGroup = "build ed25519-bip32"

fun createCopyTask(
    name: String,
    fromDir: File,
    intoDir: File
) = tasks.register<Copy>(name) {
    group = taskGroup
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    include("*.so", "*.a", "*.d", "*.dylib", "**/*.kt", "*.js")
    from(fromDir)
    into(intoDir)
}

val sourceDir = ed25519bip32Dir.resolve("wrapper").resolve("target")
val buildDir = projectDir.resolve("build").resolve("generatedResources")
val generatedDir = projectDir.resolve("build").resolve("generated")

val copyEd25519Bip32GeneratedTask = createCopyTask(
    "copyEd25519Bip32Generated",
    ed25519bip32Dir.resolve("wrapper").resolve("build").resolve("generated"),
    generatedDir
)

val copyEd25519Bip32ForMacOSX8664Task = createCopyTask(
    "copyEd25519Bip32ForMacOSX86_64",
    sourceDir.resolve("x86_64-apple-darwin").resolve("release"),
    buildDir.resolve("jvm").resolve("main").resolve("darwin-x86-64")
)

val copyEd25519Bip32ForMacOSArch64Task = createCopyTask(
    "copyEd25519Bip32ForMacOSArch64",
    sourceDir.resolve("aarch64-apple-darwin").resolve("release"),
    buildDir.resolve("jvm").resolve("main").resolve("darwin-aarch64")
)

val copyEd25519Bip32ForLinuxX8664Task = createCopyTask(
    "copyEd25519Bip32ForLinuxX86_64",
    sourceDir.resolve("x86_64-unknown-linux-gnu").resolve("release"),
    buildDir.resolve("jvm").resolve("main").resolve("linux-x86-64")
)

val copyEd25519Bip32ForLinuxArch64Task = createCopyTask(
    "copyEd25519Bip32ForLinuxArch64",
    sourceDir.resolve("aarch64-unknown-linux-gnu").resolve("release"),
    buildDir.resolve("jvm").resolve("main").resolve("linux-aarch64")
)

val copyEd25519Bip32ForAndroidX8664Task = createCopyTask(
    "copyEd25519Bip32ForAndroidX86_64",
    sourceDir.resolve("x86_64-linux-android").resolve("release"),
    buildDir.resolve("android").resolve("main").resolve("jniLibs").resolve("x86_64")
)

val copyEd25519Bip32ForAndroidArch64Task = createCopyTask(
    "copyEd25519Bip32ForAndroidArch64",
    sourceDir.resolve("aarch64-linux-android").resolve("release"),
    buildDir.resolve("android").resolve("main").resolve("jniLibs").resolve("arm64-v8a")
)

val copyEd25519Bip32ForAndroidI686Task = createCopyTask(
    "copyEd25519Bip32ForAndroidI686",
    sourceDir.resolve("i686-linux-android").resolve("release"),
    buildDir.resolve("android").resolve("main").resolve("jniLibs").resolve("x86")
)

val copyEd25519Bip32ForAndroidArmv7aTask = createCopyTask(
    "copyEd25519Bip32ForAndroidArmv7a",
    sourceDir.resolve("armv7-linux-androideabi").resolve("release"),
    buildDir.resolve("android").resolve("main").resolve("jniLibs").resolve("armeabi-v7a")
)

val copyEd25519Bip32Wrapper by tasks.register("copyEd25519Bip32") {
    dependsOn(
        copyEd25519Bip32GeneratedTask,
        copyEd25519Bip32ForMacOSX8664Task,
        copyEd25519Bip32ForMacOSArch64Task,
        copyEd25519Bip32ForLinuxX8664Task,
        copyEd25519Bip32ForLinuxArch64Task,
        copyEd25519Bip32ForAndroidX8664Task,
        copyEd25519Bip32ForAndroidArch64Task,
        copyEd25519Bip32ForAndroidI686Task,
        copyEd25519Bip32ForAndroidArmv7aTask
    )
    mustRunAfter(buildEd25519Bip32Wrapper)
}

val buildEd25519Bip32Wrapper by tasks.register<Exec>("buildEd25519Bip32Wrapper") {
    group = taskGroup
    workingDir = ed25519bip32Dir.resolve("wrapper")
    commandLine("./build-kotlin-library.sh")
}

val copyEd25519Bip32Wasm = createCopyTask(
    "copyEd25519Bip32GeneratedWasm",
    ed25519bip32Dir.resolve("wasm").resolve("build"),
    projectDir.resolve("build").resolve("js").resolve("packages").resolve("Apollo").resolve("kotlin")
)
copyEd25519Bip32Wasm.configure {
    mustRunAfter(buildEd25519Bip32Wasm)
}

val buildEd25519Bip32Wasm by tasks.register<Exec>("buildEd25519Bip32Wasm") {
    group = taskGroup
    workingDir = ed25519bip32Dir.resolve("wasm")
    commandLine("./build_kotlin_library.sh")
}

val buildEd25519Bip32Task by tasks.register("buildEd25519Bip32") {
    group = taskGroup
    dependsOn(buildEd25519Bip32Wasm, copyEd25519Bip32Wasm, buildEd25519Bip32Wrapper, copyEd25519Bip32Wrapper)
}

val cleanEd25519Bip32 by tasks.register<Delete>("cleanEd25519Bip32") {
    group = taskGroup
    val wasmDir = ed25519bip32Dir.resolve("wasm")
    delete.add(wasmDir.resolve("build"))
    delete.add(wasmDir.resolve("pkg"))
    delete.add(wasmDir.resolve("node_modules"))
    delete.add(wasmDir.resolve("target"))

    val wrapperDir = ed25519bip32Dir.resolve("wrapper")
    delete.add(wrapperDir.resolve("build"))
    delete.add(wrapperDir.resolve("target"))
}

tasks {
    getByName<Delete>("clean") {
        dependsOn(cleanEd25519Bip32)
    }

    withType<ProcessResources> {
        dependsOn(buildEd25519Bip32Task)
    }
}

afterEvaluate {
    tasks.getByName("mergeDebugJniLibFolders").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("packageDebugResources").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("extractDeepLinksForAarDebug").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("mergeReleaseJniLibFolders").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("extractDeepLinksForAarRelease").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("packageReleaseResources").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("mergeReleaseResources").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("compileKotlinJvm").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("runKtlintCheckOverAllButJSMainSourceSet").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("runKtlintCheckOverAndroidMainSourceSet").dependsOn(buildEd25519Bip32Task)
    tasks.getByName("runKtlintCheckOverJvmMainSourceSet").dependsOn(buildEd25519Bip32Task)
}
