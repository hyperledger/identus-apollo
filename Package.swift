// swift-tools-version:5.7
import PackageDescription

let package = Package(
    name: "Apollo",
    platforms: [
        .iOS(.v13),
        .macOS(.v11)
    ],
    products: [
        .library(
            name: "Apollo",
            targets: ["Apollo"]
        ),
    ],
    targets: [
        // LOCAL
        // .binaryTarget(
        //     name: "Apollo",
        //     path: "./Apollo.xcframework"
        // ),

        // RELEASE
        .binaryTarget(
            name: "Apollo",
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/testSwift1/Apollo.xcframework.zip",
            checksum: "5a375e67217481eb177f9fb25bab349bb3ada0c1c37cbebe2bdda8979387f899"
        )
    ]
)
