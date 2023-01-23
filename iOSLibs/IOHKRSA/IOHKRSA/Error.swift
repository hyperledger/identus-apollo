//
//  Error.swift
//  IOHKRSA
//
//  Created by Ahmed Moussa on 13/12/2022.
//

import Foundation
import CommonCrypto

public enum Error: Swift.Error {
    case keyGenerationFaild
    case importKeyFailed
    case exportKeyFailed
}
