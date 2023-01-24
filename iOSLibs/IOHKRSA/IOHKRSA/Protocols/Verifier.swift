//
//  Verifier.swift
//  IOHKRSA
//
//  Created by Ahmed Moussa on 10/01/2023.
//

import Foundation

public protocol Verifier {
    func verify(data: Data, signedData: Data) -> Bool
}
