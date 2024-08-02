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
            url: "https://github.com/hyperledger/identus-apollo/releases/download/v1.4.0/Apollo.xcframework.zip",
            checksum: "de744fb129cc1f60fe8ea4ac5fd511ee97a18ac596a102dc78dd25431871a704"
        )
    ]
)
