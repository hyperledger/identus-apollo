package io.iohk.atala.prism.apollo.jose

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.JWSObjectJSON
import com.nimbusds.jose.Payload
import com.nimbusds.jose.UnprotectedHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.crypto.Ed25519Signer
import com.nimbusds.jose.crypto.Ed25519Verifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.OctetKeyPair
import com.nimbusds.jose.util.Base64URL
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.SignatureException

sealed interface Key {
    val id: String
    val jwk: JWK
    val curve: Curve
}

enum class Typ(val typ: String) {
    Encrypted("application/didcomm-encrypted+json"),
    Signed("application/didcomm-signed+json"),
    Plaintext("application/didcomm-plain+json");

    companion object {
        fun parse(str: String): Typ = when (str) {
            "application/didcomm-encrypted+json" -> Encrypted
            "application/didcomm-signed+json" -> Signed
            "application/didcomm-plain+json" -> Plaintext
            else -> throw IllegalArgumentException("Unsupported message typ")
        }
    }
}

enum class SignAlg {
    /**
     * Elliptic curve digital signature with edwards curves Ed25519 and SHA-512
     */
    ED25519,

    /**
     * Elliptic curve digital signature with NIST p-256 curve and SHA-256
     */
    ES256,

    /**
     * Elliptic curve digital signature with Secp256k1 keys
     */
    ES256K
}

inline fun <reified Key> JWK.asKey(): Key {
    if (this !is Key) throw IllegalArgumentException("Can not cast JWK to ${Key::class.java.name}")
    return this
}

object JWS {
    fun sign(payload: String, key: Key): String {
        val jwk = key.jwk
        val alg = getJWSAlgorithm(jwk)

        val signer = try {
            when (alg) {
                JWSAlgorithm.ES256 -> ECDSASigner(jwk.asKey<ECKey>())
                JWSAlgorithm.ES256K -> ECDSASigner(jwk.asKey<ECKey>())
                JWSAlgorithm.EdDSA -> Ed25519Signer(jwk.asKey())
                else -> throw NotImplementedError(alg.name)
            }
        } catch (e: JOSEException) {
            throw NotImplementedError(alg.name)
        }

        val jwsProtectedHeader = JWSHeader.Builder(alg)
            .type(JOSEObjectType(Typ.Signed.typ))
            .build()

        val jwsUnprotectedHeader = UnprotectedHeader.Builder().keyID(key.id).build()
        return JWSObjectJSON(Payload(Base64URL.encode(payload)))
            .apply {
                try {
                    sign(jwsProtectedHeader, jwsUnprotectedHeader, signer)
                } catch (e: JOSEException) {
                    // this can be thrown if the signature type is not supported
                    // example: curve256k1 is not supported in JDK >= 15
                    if (e.cause is SignatureException) {
                        throw NotImplementedError("Unsupported signature algorithm ${e.cause}")
                    }
                    throw NotImplementedError("JWS cannot be signed $e")
                }
            }
            .serializeGeneral()
    }

    fun verify(signature: JWSObjectJSON.Signature, signAlg: SignAlg, key: Key) {
        val jwk = key.jwk

        val verifier = try {
            when (signAlg) {
                SignAlg.ES256 -> ECDSAVerifier(jwk.asKey<ECKey>())
                SignAlg.ES256K -> ECDSAVerifier(jwk.asKey<ECKey>())
                SignAlg.ED25519 -> Ed25519Verifier(jwk.asKey())
            }
        } catch (e: JOSEException) {
            throw NotImplementedError(signAlg.name)
        }

        try {
            if (!signature.verify(verifier))
                throw NotImplementedError("Invalid signature")
        } catch (e: JOSEException) {
            // this can be thrown if the signature type is not supported
            // example: curve256k1 is not supported in JDK >= 15
            if (e.cause is SignatureException) {
                throw NotImplementedError("Unsupported signature algorithm ${e.cause}")
            }
            throw NotImplementedError("JWS signature cannot be verified $e")
        }
    }
}

object JWT {
    fun sign(jwtClaimsSet: JWTClaimsSet, key: Key): String {
        val jwk = key.jwk
        val alg = getJWSAlgorithm(jwk)

        val signer = try {
            when (alg) {
                JWSAlgorithm.ES256 -> ECDSASigner(jwk.asKey<ECKey>())
                JWSAlgorithm.ES256K -> ECDSASigner(jwk.asKey<ECKey>())
                JWSAlgorithm.EdDSA -> Ed25519Signer(jwk.asKey())
                else -> throw NotImplementedError(alg.name)
            }
        } catch (e: JOSEException) {
            throw NotImplementedError(alg.name)
        }

        val jwsHeader = JWSHeader.Builder(alg)
            .keyID(key.id)
            .build()

        return SignedJWT(jwsHeader, jwtClaimsSet).apply {
            try {
                sign(signer)
            } catch (e: JOSEException) {
                throw NotImplementedError("JWT cannot be signed $e")
            }
        }.serialize()
    }
}

private fun getCryptoAlg(signature: JWSObjectJSON.Signature): SignAlg {
    return when (val alg = signature.header.algorithm) {
        JWSAlgorithm.ES256 -> SignAlg.ES256
        JWSAlgorithm.ES256K -> SignAlg.ES256K
        JWSAlgorithm.EdDSA -> SignAlg.ED25519
        else -> throw NotImplementedError("UnsupportedCurveException ${alg.name}")
    }
}

private fun getJWSAlgorithm(jwk: JWK): JWSAlgorithm {
    return when (jwk) {
        is ECKey -> {
            when (jwk.curve) {
                Curve.P_256 -> JWSAlgorithm.ES256
                Curve.SECP256K1 -> JWSAlgorithm.ES256K
                else -> {
                    throw NotImplementedError("UnsupportedCurveException ${jwk.curve}")
                }
            }
        }
        is OctetKeyPair -> {
            when (jwk.curve) {
                Curve.Ed25519 -> JWSAlgorithm.EdDSA
                else -> throw NotImplementedError("UnsupportedCurveException ${jwk.curve.name}")
            }
        }
        else -> throw NotImplementedError("UnsupportedCurveException ${jwk.javaClass.name}")
    }
}

private fun getCryptoAlg(jws: JWSObject): SignAlg {
    return when (val alg = jws.header.algorithm) {
        JWSAlgorithm.ES256 -> SignAlg.ES256
        JWSAlgorithm.ES256K -> SignAlg.ES256K
        JWSAlgorithm.EdDSA -> SignAlg.ED25519
        else -> throw NotImplementedError(alg.name)
    }
}