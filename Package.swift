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
            url: "https://github.com/hyperledger/identus-apollo/releases/download/v1.4.0-beta.4/Apollo.xcframework.zip",
            checksum: "7a7070fe87ad4e9d5f26eb11e080183f7e02d40f6fd7c9a276cee7aeea9f89b0"
        )
    ]
)
