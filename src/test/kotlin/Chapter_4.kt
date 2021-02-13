import io.kotest.assertions.throwables.shouldNotThrowAnyUnit
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger

class Chapter_4 : StringSpec({
    "ex 4.1" {
        fun inc(n: Int) = n + 1
        fun dec(n: Int) = n - 1
        tailrec fun add(a: Int, b: Int): Int = if (b == 0) a else add(inc(a), dec(b))
        add(3, 7) shouldBe 10
    }
    "ex 4.2" {
        val test = object {
            val factorial: (Int) -> Int by lazy { { i -> if (i == 0) 1 else i * factorial(i - 1) } }
        }
        test.factorial(0) shouldBe 1
        test.factorial(2) shouldBe 2
        test.factorial(4) shouldBe 24
    }
    "ex 4.3" {
        fun fib(n: Int): BigInteger {
            tailrec fun process(prev1: BigInteger, prev2: BigInteger, current: Int): BigInteger {
                return if (current == n) prev1 + prev2
                else process(prev1 + prev2, prev1, current + 1)
            }
            return if (n < 2) BigInteger.ONE else process(BigInteger.ONE, BigInteger.ONE, 2)
        }
        (0..10).map { fib(it) } shouldBe (listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89).map { BigInteger.valueOf(it.toLong()) })
        shouldNotThrowAnyUnit { fib(20_000) } // verify tailrec is working
    }
})