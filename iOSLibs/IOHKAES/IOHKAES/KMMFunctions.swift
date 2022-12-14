//
//  KMMFunctions.swift
//  IOHKAES
//
//  Created by Ahmed Moussa on 09/11/2022.
//

import Foundation

@objc public class KMMFunctions: NSObject {
    private override init() {}

    @objc public class func aesEncryption(algorithm: AESAlgorithm, options: AESOptions, mode: BlockMode, padding: Padding, data: Data, key: Data, iv: Data? = nil) -> Data? {
        let encryptor = try! AESEncryptor(algorithm: algorithm, options: options, mode: mode, padding: padding, key: key, iv: iv ?? Data())
        return encryptor.encrypt(data: data)
    }

    @objc public class func aesDecryption(algorithm: AESAlgorithm, options: AESOptions, mode: BlockMode, padding: Padding, data: Data, key: Data, iv: Data? = nil) -> Data? {
        let decryptor = try! AESDecryptor(algorithm: algorithm, options: options, mode: mode, padding: padding, key: key, iv: iv ?? Data())
        return decryptor.decrypt(data: data)
    }
    
    @objc public class func generateAESKey(algorithm: AESAlgorithm) -> Data {
        return generateSymmetricEncryptionKey(algorithm: algorithm)
    }
    
    ///
    /// Generate AES encryption Key
    ///
    private class func generateSymmetricEncryptionKey(algorithm: AESAlgorithm) -> Data {
        var keyData = Data(count: algorithm.defaultKeySize)
        _ = keyData.withUnsafeMutableBytes {
            SecRandomCopyBytes(kSecRandomDefault, algorithm.defaultKeySize, $0.baseAddress!)
        }
        return keyData
    }
}
