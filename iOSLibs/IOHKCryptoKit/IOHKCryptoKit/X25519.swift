import CryptoKit
import Foundation

@objc public class X25519: NSObject {

    @objc public class func createPrivateKey() -> DataWithError {
        DataWithError {
            Curve25519.KeyAgreement.PrivateKey().rawRepresentation
        }
    }

    @objc public class func publicKey(privateKey: Data) -> DataWithError {
        DataWithError {
            try Curve25519.KeyAgreement.PrivateKey(rawRepresentation: privateKey).publicKey.rawRepresentation
        }
    }
}
