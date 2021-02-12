import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

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
})