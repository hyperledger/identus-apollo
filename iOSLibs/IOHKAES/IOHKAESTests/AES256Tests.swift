//
//  AES256Tests.swift
//  IOHKAESTests
//
//  Created by Ahmed Moussa on 13/10/2022.
//

import XCTest
@testable import IOHKAES
import CommonCrypto

class AES256Tests: XCTestCase {
    
    var sourceData: Data!
    var iv: Data!
    var key: Data!
    var aesEncryptor: AESEncryptor!
    var aesDecryptor: AESDecryptor!
    var sourcedString: String!
    
    override func setUp() {
        super.setUp()
        do {
            sourcedString = "+93654HJU)(ˆ% Hello AES256"
            
            sourceData = sourcedString.data(using: .utf8)!
            // print(sourceData.base64EncodedData())
            iv = randomIv(length: kCCBlockSizeAES128)
            key = IOHKAES.generateAESKey(algorithm: .aes256)
            aesEncryptor = try AESEncryptor(algorithm: .aes256, options: .pkcs7Padding, mode: .cbc, padding: .pkcs7Padding, key: key, iv: iv)
            aesDecryptor = try AESDecryptor(algorithm: .aes256, options: .pkcs7Padding, mode: .cbc, padding: .pkcs7Padding, key: key, iv: iv)
        } catch let error {
            XCTFail(error.localizedDescription)
        }
    }
    
    func test_aes_gcm() {
        do {
            let keyGCM = IOHKAES.generateAESKey(algorithm: .aes256)
            let ivGCM = randomIv(length: kCCBlockSizeAES128)
            let aesGCMEncryptor = try AESEncryptor(algorithm: .aes256, options: .none, mode: .gcm, padding: .noPadding, key: keyGCM, iv: ivGCM)
            let aesGCMDecryptor = try AESDecryptor(algorithm: .aes256, options: .none, mode: .gcm, padding: .noPadding, key: keyGCM, iv: ivGCM)
            let encryptedData: Data? = aesGCMEncryptor.encrypt(data: sourceData)
            let decryptedData: Data? = aesGCMDecryptor.decrypt(data: encryptedData!)
            XCTAssertEqual(sourceData, decryptedData)
        } catch let error {
            XCTFail(error.localizedDescription)
        }
    }
    
    func test_encrypt_parametersData_returnData() {
        let encryptedData: Data? = aesEncryptor.encrypt(data: sourceData)
        let decryptedData: Data? = aesDecryptor.decrypt(data: encryptedData!)
        XCTAssertEqual(sourceData, decryptedData)
    }
    
    func test_encrypt_parametersStringAndEncoding_returnString() {
        let encryptedString: String? = aesEncryptor.encrypt(str: sourcedString, encoding: .utf8)
        let decryptedString: String? = aesDecryptor.decrypt(str: encryptedString!, encoding: .utf8)
        XCTAssertEqual(sourcedString, decryptedString)
    }
    
    func test_encrypt_parametersStringAndEncoding_returnData() {
        let encryptedData: Data? = aesEncryptor.encrypt(str: sourcedString, encoding: .utf8)
        let decryptedData: Data? = aesDecryptor.decrypt(data: encryptedData!)
        XCTAssertEqual(sourceData , decryptedData)
    }
    
    func test_decrypt_parametersData_returnData() {
        let encryptedData: Data? = aesEncryptor.encrypt(data: sourceData)
        let decryptedData: Data? = aesDecryptor.decrypt(data: encryptedData!)
        XCTAssertEqual(sourceData, decryptedData)
    }
    
    func test_decrypt_parametersString_returnData() {
        let encryptedString: String? = aesEncryptor.encrypt(str: sourcedString, encoding: .utf8)
        let decryptedData: Data? = aesDecryptor.decrypt(str: encryptedString!)
        XCTAssertEqual(sourceData, decryptedData)
    }
    
    func test_decrypt_parametersStringAndEncoding_returnString() {
        let encryptedString: String? = aesEncryptor.encrypt(str: sourcedString, encoding: .utf8)
        let decryptedString: String? = aesDecryptor.decrypt(str: encryptedString!, encoding: .utf8)
        XCTAssertEqual(sourcedString, decryptedString)
    }
    
    // MARK: - Helper methods
    
    ///
    /// Generate random IV with provided length
    /// - Parameter length: length to the generated iv
    /// - Returns: random generated IV
    ///
    private func randomIv(length: Int) -> Data {
        var data = Data(count: length)
        let status = data.withUnsafeMutableBytes { rawBufferPointer in
            let mutableBytes = rawBufferPointer.baseAddress!
            return SecRandomCopyBytes(kSecRandomDefault, length, mutableBytes)
        }
        assert(status == Int32(0))
        return data
    }
}
