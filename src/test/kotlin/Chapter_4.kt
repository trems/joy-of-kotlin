import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldNotThrowAnyUnit
import io.kotest.assertions.throwables.shouldThrowAny
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
    "ex 4.4" {
        /* from recursive */
        fun <T> makeString(list: List<T>, delim: String): String =
                when {
                    list.isEmpty() -> ""
                    list.drop(1).isEmpty() -> "${list.first()}${makeString(list.drop(1), delim)}"
                    else -> "${list.first()}$delim${makeString(list.drop(1), delim)}"
                }
        /* to tail-recursive */
        fun <T> joinStr(list: List<T>, delim: String): String {
            tailrec fun process(acc: String, lst: List<T>): String =
                    when {
                        lst.isEmpty() -> acc
                        acc.isEmpty() -> process("${lst.first()}", lst.drop(1))
                        else -> process("$acc$delim${lst.first()}", lst.drop(1))
                    }
            return process("", list)
        }
        makeString(listOf(1,2,3,4,5), ",") shouldBe "1,2,3,4,5"
        joinStr(listOf(1,2,3,4,5), ",") shouldBe "1,2,3,4,5"

        val numbers = generateSequence(1) { it + 1 }.take(20_000).toList()
        shouldThrowAny { makeString(numbers, ",") }
        shouldNotThrowAny { joinStr(numbers, ",") }
    }
    "ex 4.5" {
        fun <T, U> foldLeft(list: List<T>, initAcc: U, aggregate: (T, U) -> U): U {
            tailrec fun process(lst: List<T>, acc: U): U =
                    when {
                        lst.isEmpty() -> acc
                        else -> process(lst.drop(1), aggregate(lst.first(), acc))
                    }
            return process(list, initAcc)
        }
        foldLeft(listOf('K','o','t','l','i','n'), "") { ch, s -> "$s$ch"} shouldBe "Kotlin"
        foldLeft(listOf(1,2,3,4,5), "") { i, s ->
            if (s.isEmpty()) "$i" else "$s,$i"} shouldBe "1,2,3,4,5"
        foldLeft(listOf(1,2,3,4,5), 0) { i, sum -> sum + i } shouldBe 15
    }
    "ex 4.6" {
        fun <T, U> foldRight(list: List<T>, identity: U, foldFun: (U, T) -> U): U =
            when {
                list.isEmpty() -> identity
                else -> foldFun(foldRight(list.drop(1), identity, foldFun), list.first())
            }
        fun string(chars: List<Char>): String = foldRight(chars, "") { s, ch -> "$ch$s" }
        string(listOf('K','o','t','l','i','n')) shouldBe "Kotlin"
    }
})