//
//  RSASigner.swift
//  IOHKRSA
//
//  Created by Ahmed Moussa on 10/01/2023.
//

import Foundation

public class RSASigner: Signer, Verifier {
    private var key: SecKey
    public private(set) var type: RSASignatureMessageType
    
    public init(key: SecKey, type: RSASignatureMessageType) {
        self.key = key
        self.type = type
    }
    
    // MARK: - Signer
    public func sign(data: Data) -> Data? {
        let cfData = data as CFData
        var error: Unmanaged<CFError>?
        guard let signedData = SecKeyCreateSignature(key, type.nativeValue, cfData, &error) as Data? else {
            return nil
        }
        return signedData
    }
    
    // MARK: - Verifier
    public func verify(data: Data, signedData: Data) -> Bool {
        let cfData = data as CFData
        let cfSignedData = signedData as CFData
        var error: Unmanaged<CFError>?
        return SecKeyVerifySignature(key, type.nativeValue, cfData, cfSignedData, &error)
    }
}
