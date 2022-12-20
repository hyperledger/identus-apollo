//
//  Error.swift
//  IOHKAES
//
//  Created by Ahmed Moussa on 13/12/2022.
//

import Foundation
import CommonCrypto

public enum Error: Swift.Error {
    case invalidKeySize
    case invalidIVSizeOrLength
    case cryptoFailed(status: CCCryptorStatus)
    case cryptoFailed(description: String)
}
