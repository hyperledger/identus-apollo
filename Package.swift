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
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/1.0.6/Apollo.xcframework.zip",
            checksum: "647ca0fc88878545eb80981e62509ff465bf16d8bd951fe7019587a0ad51dd24"
        )
    ]
)
