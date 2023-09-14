package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import io.iohk.atala.prism.apollo.utils.Validated
import io.iohk.atala.prism.apollo.utils.decodeHex
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

private typealias Hash = ByteArray

// Bitmask index representing leaf position in a tree where unset i-th bit means that the leaf is
// located in the left branch of the i-th node starting from the root and vice-versa
private typealias Index = Int

// In order to defend against second-preimage attack we prefix node hashes with either 0 or 1
// depending on the type of the node (leaf or internal node)
internal const val LeafPrefix: Byte = 0
internal const val NodePrefix: Byte = 1

// Merkle tree is a tree where every leaf node is labeled with SHA256 hash of some external data
// block,and every non-leaf node is labeled with SHA256 hash of the concatenation of its child
// nodes. The tree does not have to be full in the bottom level (i.e. leaves can differ in
// height), but the maximum leaf height is still limited by O(log(N)) where N is the number of
// leaves.
//
// Note that our MerkleTrees are immutable and hence can only be created by supplying the entire
// list of leaf nodes' hashes at once.
private sealed class MerkleTree {
    abstract val hash: Hash
}

private data class MerkleNode(val left: MerkleTree, val right: MerkleTree) : MerkleTree() {
    override val hash: Hash = combineHashes(left.hash, right.hash)
}

private data class MerkleLeaf(val data: Hash) : MerkleTree() {
    override val hash: Hash = prefixHash(data)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
data class MerkleRoot(val hash: Hash)

// Cryptographic proof of the given hash's inclusion in the Merkle tree which can be verified
// by anyone.
@OptIn(ExperimentalJsExport::class)
@JsExport
data class MerkleInclusionProof(
    val hash: Hash, // hash inclusion of which this proof is for
    val index: Index, // index for the given hash's position in the tree
    val siblings: Array<Hash> // given hash's siblings at each level of the tree starting from the bottom
) {
    // merkle root of which this proof is for
    fun derivedRoot(): MerkleRoot {
        val n = siblings.size
        val root = siblings.indices.fold(prefixHash(hash)) { currentHash, i ->
            if (index and (1 shl (n - i - 1)) == 0) {
                combineHashes(currentHash, siblings[i])
            } else {
                combineHashes(siblings[i], currentHash)
            }
        }

        return MerkleRoot(root)
    }

    fun toJson(): JsonObject {
        return JsonObject(
            mapOf(
                Pair(hashField, JsonPrimitive(hash.toHexString())),
                Pair(indexField, JsonPrimitive(index)),
                Pair(siblingsField, JsonArray(siblings.map { JsonPrimitive(it.toHexString()) }))
            )
        )
    }

    fun encode(): String {
        return toJson().toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MerkleInclusionProof

        if (!hash.contentEquals(other.hash)) return false
        if (index != other.index) return false
        if (!siblings.contentDeepEquals(other.siblings)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hash.contentHashCode()
        result = 31 * result + index
        result = 31 * result + siblings.contentDeepHashCode()
        return result
    }

    companion object {
        internal const val hashField = "hash"
        internal const val indexField = "index"
        internal const val siblingsField = "siblings"

        @JvmStatic
        fun decode(encodedMerkleInclusionProof: String): MerkleInclusionProof =
            decodeJson(Json.parseToJsonElement(encodedMerkleInclusionProof).jsonObject).getElseThrow {
                IllegalArgumentException(
                    it
                )
            }

        @JvmStatic
        fun decodeJson(encodedMerkleInclusionProof: JsonObject): Validated<MerkleInclusionProof, String> {
            val maybeHash = encodedMerkleInclusionProof[hashField]?.jsonPrimitive?.content?.decodeHex()
            val maybeIndex = encodedMerkleInclusionProof[indexField]?.jsonPrimitive?.int
            val maybeSiblings = encodedMerkleInclusionProof[siblingsField]?.jsonArray?.map { it.jsonPrimitive.content }
                ?.map { it.decodeHex() }

            return Validated.Applicative.apply(
                Validated.getOrError(
                    maybeHash,
                    "$hashField field is missing from encoded MerkleInclusionProof. encodedMerkleInclusionProof=$encodedMerkleInclusionProof"
                ),
                Validated.getOrError(
                    maybeIndex,
                    "$indexField field is missing from encoded MerkleInclusionProof. encodedMerkleInclusionProof=$encodedMerkleInclusionProof"
                ),
                Validated.getOrError(
                    maybeSiblings,
                    "$siblingsField field is missing from encoded MerkleInclusionProof. encodedMerkleInclusionProof=$encodedMerkleInclusionProof"
                )
            ) { hash, index, siblings ->
                Validated.Valid(
                    MerkleInclusionProof(hash, index, siblings.toTypedArray())
                )
            }
        }
    }
}

private fun combineHashes(left: Hash, right: Hash): Hash {
    return SHA256().digest(byteArrayOf(NodePrefix) + (left + right))
}

private fun prefixHash(data: Hash): Hash {
    return SHA256().digest(byteArrayOf(LeafPrefix) + data)
}

data class MerkleProofs(val root: MerkleRoot, val proofs: List<MerkleInclusionProof>)

fun generateProofs(hashes: List<Hash>): MerkleProofs {
    tailrec fun buildMerkleTree(currentLevel: List<MerkleTree>, nextLevel: List<MerkleTree>): MerkleTree {
        return when {
            currentLevel.size >= 2 -> buildMerkleTree(
                currentLevel = currentLevel.subList(2, currentLevel.size),
                nextLevel = listOf(MerkleNode(currentLevel[0], currentLevel[1])) + nextLevel
            )
            currentLevel.size == 1 -> buildMerkleTree(currentLevel = emptyList(), nextLevel = listOf(currentLevel[0]) + nextLevel)
            nextLevel.size == 1 -> nextLevel[0]

            // We reverse `nextLevel` list so that it has the same order as the initial
            // `currentLevel` list
            else -> buildMerkleTree(currentLevel = nextLevel.reversed(), nextLevel = emptyList())
        }
    }

    fun buildProofs(tree: MerkleTree, currentIndex: Index, currentPath: List<Hash>): List<MerkleInclusionProof> {
        return when (tree) {
            is MerkleLeaf -> listOf(MerkleInclusionProof(tree.data, currentIndex, currentPath.toTypedArray()))
            is MerkleNode -> {
                val first = buildProofs(
                    tree.left,
                    currentIndex,
                    listOf(tree.right.hash) + currentPath
                )
                val second = buildProofs(
                    tree.right,
                    currentIndex or (1 shl currentPath.size),
                    listOf(tree.left.hash) + currentPath
                )
                first + second
            }
        }
    }

    require(hashes.isNotEmpty())

    val merkleTree = buildMerkleTree(hashes.map { MerkleLeaf(it) }, emptyList())
    val merkleProofs = buildProofs(merkleTree, 0, emptyList())

    return MerkleProofs(MerkleRoot(merkleTree.hash), merkleProofs)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun verifyProof(root: MerkleRoot, proof: MerkleInclusionProof): Boolean {
    // Proof length should not exceed 31 as 2^31 is the maximum size of Merkle tree
    return proof.siblings.size < 31 && proof.derivedRoot() == root
}
