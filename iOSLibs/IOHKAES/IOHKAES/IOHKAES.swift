//
//  IOHKAES.swift
//  IOHKAES
//
//  Created by Ahmed Moussa on 15/12/2022.
//

import Foundation
import CryptoKit

@objc public class IOHKAES: NSObject {
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
        let key = SymmetricKey(size: algorithm.gcmKeySize)
        let keyData = key.withUnsafeBytes {
            return Data(Array($0))
        }
        return keyData
    }
}
