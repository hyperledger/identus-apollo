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
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/<ref>/Apollo.xcframework.zip",
            checksum: "<checksum>"
        )
    ]
)
