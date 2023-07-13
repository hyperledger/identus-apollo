import CryptoKit
import Foundation

@objc public class X25519: NSObject {

    @objc public class func createPrivateKey() -> Data {
        Curve25519.KeyAgreement.PrivateKey().rawRepresentation
    }

    @objc public class func publicKey(privateKey: Data) throws -> Data {
        try Curve25519.KeyAgreement.PrivateKey(rawRepresentation: privateKey).publicKey.rawRepresentation
    }
}
