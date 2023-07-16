import CryptoKit
import Foundation

@objc public class Ed25519: NSObject {
    @objc public class func createPrivateKey() -> Data {
        Curve25519.Signing.PrivateKey().rawRepresentation
    }

    @objc public class func sign(privateKey: Data, data: Data) throws -> Data {
        try Curve25519.Signing.PrivateKey(rawRepresentation: privateKey).signature(for: data)
    }

    @objc public class func publicKey(privateKey: Data) throws -> Data {
        try Curve25519.Signing.PrivateKey(rawRepresentation: privateKey).publicKey.rawRepresentation
    }

    @objc public class func verify(publicKey: Data, signature: Data, data: Data) throws -> NSNumber {
        return NSNumber(booleanLiteral: try Curve25519
            .Signing
            .PublicKey(rawRepresentation: publicKey)
            .isValidSignature(signature, for: data)
        )
    }
}
