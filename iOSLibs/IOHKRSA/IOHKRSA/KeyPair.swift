//
//  KeyPair.swift
//  IOHKRSA
//
//  Created by Ahmed Moussa on 23/01/2023.
//

import Foundation

@objc public class KeyPair: NSObject {
    @objc public let publicKey: SecKey
    @objc public let privateKey: SecKey
    
    @objc public init(privateKey: SecKey, publicKey: SecKey) {
        self.privateKey = privateKey
        self.publicKey = publicKey
        super.init()
    }
}
