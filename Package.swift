// swift-tools-version:5.7
import PackageDescription

let package = Package(
    name: "ApolloLibrary",
    platforms: [
        .iOS(.v13),
        .macOS(.v11)
    ],
    products: [
        .library(
            name: "ApolloLibrary",
            targets: ["ApolloBinary"]
        ),
    ],
    targets: [
        // LOCAL
        // .binaryTarget(
        //     name: "ApolloBinary",
        //     path: "./base-asymmetric-encryption/build/packages/ApolloSwift/Apollo.xcframework.zip"
        // ),

        // RELEASE
        .binaryTarget(
            name: "ApolloBinary",
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/v1.2.8/Apollo.xcframework.zip",
            checksum: "19b72a36e93ec61b8fcd55f0974d2d88116089bff09f88b119fbd19f69770717"
        )
    ]
)
