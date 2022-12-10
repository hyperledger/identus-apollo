//
//  SwiftCryptoKit.swift
//  SwiftCryptoKit
//
//  Created by Mariusz Ołownia on 12/03/2021.
//

import Foundation
import CryptoKit

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

@objc class SwiftCryptoKit : NSObject {
    @objc public class func aes256GcmEncrypt(data: Data, key: Data, iv: Data) -> DataWithError {
        DataWithError {
            let symmetricKey = SymmetricKey(data: key)
            let aesNonce = try AES.GCM.Nonce.init(data: iv)
            let sealedBox = try AES.GCM.seal(data, using: symmetricKey, nonce: aesNonce)
            return sealedBox.ciphertext + sealedBox.tag
        }
    }
    
    @objc public class func aes256GcmDecrypt(data: Data, key: Data, iv: Data) -> DataWithError {
        DataWithError {
            let symmetricKey = SymmetricKey(data: key)
            let aesNonce = try AES.GCM.Nonce.init(data: iv)
            
            let tagIndex = data.endIndex - 16
            let ciphertext = data[data.startIndex ..< tagIndex]
            let tag = data[tagIndex ..< data.endIndex]
            
            let sealedBox = try AES.GCM.SealedBox(nonce: aesNonce, ciphertext :ciphertext, tag: tag)
            return try AES.GCM.open(sealedBox, using: symmetricKey)
        }
    }
}
