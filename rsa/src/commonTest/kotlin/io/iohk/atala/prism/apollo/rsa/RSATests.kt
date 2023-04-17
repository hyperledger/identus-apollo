package io.iohk.atala.prism.apollo.rsa

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult

@OptIn(ExperimentalCoroutinesApi::class)
expect class RSATests {
    fun testRSA(): TestResult
    fun testRSAPSS(): TestResult
}
