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
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/1.0.3/Apollo.xcframework.zip",
            checksum: "89989b558887431844d5395c845a098678577b3c1dc867146ca058f0ce9e63dd"
        )
    ]
)
