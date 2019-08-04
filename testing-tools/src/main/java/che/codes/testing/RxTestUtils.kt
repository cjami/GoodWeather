package che.codes.testing

import io.reactivex.Single

object RxTestUtils {
    fun <T> getResult(single: Single<T>): T {
        val testObserver = single.test()
        testObserver.await()
        return testObserver.values()[0]
    }

    fun <T> waitForTerminalEvent(single: Single<T>) {
        val testObserver = single.test()
        testObserver.await()
    }

    fun <T> assertError(single: Single<T>, error: Throwable) {
        val testObserver = single.test()
        testObserver.await()
        testObserver.assertError(error)
    }
}