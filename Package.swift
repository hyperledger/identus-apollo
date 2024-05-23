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
            url: "https://github.com/input-output-hk/atala-prism-apollo/releases/download/v1.3.1/Apollo.xcframework.zip",
            checksum: "0da41f43b01188b255f780e7b297eb98aa13cc814e3657952bc0aaf06c6ed532"
        )
    ]
)
