package io.iohk.atala.prism.apollo.utils

/**
 * This class represents a value of two possible types (a disjoint union). A common use for such
 * data structure is to return it from a method that can return an error where it does not make
 * sense to throw it.
 *
 * Migrated from v1.4
 * @author Bassam Riman
 */
public sealed class Validated<out R, E> {
    public data class Valid<out R, E>(override val result: R) : Validated<R, E>()
    public data class Invalid<out R, E>(override val error: E) : Validated<R, E>()

    public open val result: R? = null
    public open val error: E? = null

    public fun toTuple(): Pair<R?, E?> = Pair(result, error)

    public fun <R1> flatMap(map: (r: R) -> Validated<R1, E>): Validated<R1, E> =
        when (this) {
            is Valid -> map(this.result)
            is Invalid -> Invalid(this.error)
        }

    public suspend fun <R1> suspendableFlatMap(map: suspend (r: R) -> Validated<R1, E>): Validated<R1, E> =
        when (this) {
            is Valid -> map(this.result)
            is Invalid -> Invalid(this.error)
        }

    public fun <R1> map(map: (r: R) -> R1): Validated<R1, E> =
        when (this) {
            is Valid -> Valid(map(this.result))
            is Invalid -> Invalid(this.error)
        }

    public suspend fun <R1> suspendableMap(map: suspend (r: R) -> R1): Validated<R1, E> =
        when (this) {
            is Valid -> Valid(map(this.result))
            is Invalid -> Invalid(this.error)
        }

    public fun getElseThrow(errorToExceptionMapper: (E) -> RuntimeException): R =
        when (this) {
            is Valid -> this.result
            is Invalid -> throw errorToExceptionMapper(this.error)
        }

    public object Applicative {
        public fun <R, R1, R2, E> apply(
            validated1: Validated<R1, E>,
            validated2: Validated<R2, E>,
            apply: (r1: R1, r2: R2) -> Validated<R, E>
        ): Validated<R, E> = validated1.flatMap {
            val r1 = it
            validated2.flatMap {
                val r2 = it
                apply(r1, r2)
            }
        }

        public fun <R, R1, R2, R3, E> apply(
            validated1: Validated<R1, E>,
            validated2: Validated<R2, E>,
            validated3: Validated<R3, E>,
            apply: (r1: R1, r2: R2, r3: R3) -> Validated<R, E>
        ): Validated<R, E> =
            validated1.flatMap {
                val r1 = it
                validated2.flatMap {
                    val r2 = it
                    validated3.flatMap {
                        val r3 = it
                        apply(r1, r2, r3)
                    }
                }
            }
    }

    public companion object {
        public fun <R, E> tryOrInvalid(
            t: () -> R,
            error: E
        ): Validated<R, E> {
            return try {
                Valid(t.invoke())
            } catch (e: Throwable) {
                Invalid(error)
            }
        }

        public fun <R, E> getOrError(
            t: R?,
            error: E
        ): Validated<R, E> {
            return t?.let { Valid(it) } ?: Invalid(error)
        }

        public fun <E> validate(
            predicate: Boolean,
            error: E
        ): Validated<Unit, E> {
            return if (predicate) Valid(Unit) else Invalid(error)
        }
    }
}
