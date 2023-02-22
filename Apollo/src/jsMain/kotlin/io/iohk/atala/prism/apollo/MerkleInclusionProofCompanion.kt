package io.iohk.atala.prism.apollo

@OptIn(ExperimentalJsExport::class)
@JsExport
object MerkleInclusionProofCompanion {
    fun decode(encoded: String): MerkleInclusionProof {
        return MerkleInclusionProof.decode(encoded)
    }
}
