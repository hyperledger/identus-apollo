//
//  KMMFunctions.swift
//  IOHKSecureRandomGeneration
//
//  Created by Ahmed Moussa on 12/12/2022.
//

import Foundation

@objc public class KMMFunctions: NSObject {
    private override init() {}
    
    ///
    /// Generate random IV with provided length
    /// - Parameter length: length to the generated iv
    /// - Returns: random generated IV
    ///
    @objc public class func randomIV(_ length: Int) -> Data {
        return randomData(length: length)
    }
    
    ///
    /// Generate random Salt with provided length
    /// - Parameter length: length to the generated Salt
    /// - Returns: random generated Salt
    ///
    @objc public class func randomSalt(_ length: Int) -> Data {
        return randomData(length: length)
    }
    
    ///
    /// Generate random Data with provided length
    /// - Parameter length: length to the generated Data
    /// - Returns: random generated Data
    ///
    private class func randomData(length: Int) -> Data {
        var data = Data(count: length)
        let status = data.withUnsafeMutableBytes { rawBufferPointer in
            let mutableBytes = rawBufferPointer.baseAddress!
            return SecRandomCopyBytes(kSecRandomDefault, length, mutableBytes)
        }
        assert(status == Int32(0))
        return data
    }
}
