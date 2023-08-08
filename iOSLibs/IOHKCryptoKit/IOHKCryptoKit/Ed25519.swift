import CryptoKit
import Foundation

@objc public class DataWithError: NSObject {

    @objc public private(set) var success: Data?
    @objc public private(set) var failure: Error?

    private init(_ success: Data?, _ failure: Error?) {
        super.init()
        self.success = success
        self.failure = failure
    }

    public convenience init(success: Data) {
        self.init(success, nil)
    }
    public convenience init(failure: Error) {
        self.init(nil, failure)
    }
    public convenience init(_ block: () throws -> Data) {
        do {
            let data = try block()
            self.init(success: data)
        } catch {
            self.init(failure: error)
        }
    }
}

@objc public class NumberWithError: NSObject {

    @objc public private(set) var success: NSNumber?
    @objc public private(set) var failure: Error?

    private init(_ success: NSNumber?, _ failure: Error?) {
        super.init()
        self.success = success
        self.failure = failure
    }

    public convenience init(success: NSNumber) {
        self.init(success, nil)
    }
    public convenience init(failure: Error) {
        self.init(nil, failure)
    }
    public convenience init(_ block: () throws -> NSNumber) {
        do {
            let data = try block()
            self.init(success: data)
        } catch {
            self.init(failure: error)
        }
    }
}


@objc public class Ed25519: NSObject {
    @objc public class func createPrivateKey() -> DataWithError {
        DataWithError {
            Curve25519.Signing.PrivateKey().rawRepresentation
        }
    }

    @objc public class func sign(privateKey: Data, data: Data) -> DataWithError {
        DataWithError {
            try Curve25519.Signing.PrivateKey(rawRepresentation: privateKey).signature(for: data)
        }
    }

    @objc public class func publicKey(privateKey: Data) -> DataWithError {
        DataWithError {
            try Curve25519.Signing.PrivateKey(rawRepresentation: privateKey).publicKey.rawRepresentation
        }
    }

    @objc public class func verify(publicKey: Data, signature: Data, data: Data) -> NumberWithError {
        NumberWithError {
            NSNumber(booleanLiteral: try Curve25519
                .Signing
                .PublicKey(rawRepresentation: publicKey)
                .isValidSignature(signature, for: data)
            )
        }
    }
}
