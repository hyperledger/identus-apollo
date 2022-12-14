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
    /// Generate random Data with provided length
    /// - Parameter length: length to the generated Data
    /// - Returns: random generated Data
    ///
    @objc public class func randomData(length: Int) -> Data {
        var data = Data(count: length)
        let status = data.withUnsafeMutableBytes { rawBufferPointer in
            let mutableBytes = rawBufferPointer.baseAddress!
            return SecRandomCopyBytes(kSecRandomDefault, length, mutableBytes)
        }
        assert(status == Int32(0))
        return data
    }
}
