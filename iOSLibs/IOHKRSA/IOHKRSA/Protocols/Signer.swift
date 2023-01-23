//
//  Signer.swift
//  IOHKRSA
//
//  Created by Ahmed Moussa on 10/01/2023.
//

import Foundation

public protocol Signer {
    func sign(data: Data) -> Data?
}
