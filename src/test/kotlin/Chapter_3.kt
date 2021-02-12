import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Chapter_3 : StringSpec({
    val square: (Int) -> Long = { it * 2L }
    val triple: (Double) -> Int = { (it * 3).toInt() }

    "ex 3.2" {
        fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { f(g(it)) }
        compose<String, Int, Int>({ x -> x * 2 }, { x -> x.length % 2})("kotlin") shouldBe 0
    }
    "ex 3.3" {
        val add: (Int) -> (Int) -> Int = { a -> { b -> a + b }}
        add(3)(2) shouldBe 5
    }
    "ex 3.4" {
        val compose: ((Int) -> Long) -> ((Double) -> Int) -> (Double) -> Long =
            { x -> { y -> { z -> x(y(z)) }}}
        val squareOfTriple: (Double) -> Long = compose(square)(triple)
        squareOfTriple(2.0) shouldBe 12L
    }
    "ex 3.5" {
        fun <T, U, V> higherCompose(): ((U) -> V) -> ((T) -> U) -> (T) -> V =
            { x: (U) -> V -> { y: (T) -> U -> { z: T -> x(y(z)) }} }
        val squareOfTriple: (Double) -> Long = higherCompose<Double, Int, Long>()(square)(triple)
        squareOfTriple(2.0) shouldBe 12L
    }
    "ex 3.6" {
        fun <T, U, V> higherAndThen() =
            { x: (T) -> U -> { y: (U) -> V -> { z: T -> y(x(z)) }}}
        val squareOfTriple: (Double) -> Long = higherAndThen<Double, Int, Long>()(triple)(square)
        squareOfTriple(2.0) shouldBe 12L
    }
    "ex 3.7" {
        fun <A, B, C> partially(a: A, f: (A) -> (B) -> C): (B) -> C = f(a)
        val f: (Long) -> String = partially<Int, Long, String>(2) { a: Int -> { l: Long -> (l + a).toString() } }
        f(3L) shouldBe "5"
    }
    "ex 3.8" {
        fun <A, B, C> partial(b: B, f: (A) -> (B) -> C): (A) -> C = { a: A -> f(a)(b) }
        val f = partial<Int, Long, String>(10L, { i: Int -> { l: Long -> (l * i).toString() } })
        f(2) shouldBe "20"
    }
    "ex 3.9" {
        fun <A, B, C, D> funcFrom(a: A, b: B, c: C, d: D): String = "$a $b $c $d"
        fun <A, B, C, D> funcToCurried(): (A) -> (B) -> (C) -> (D) -> String = {a -> {b -> {c -> {d -> "$a $b $c $d" }}}}
        funcFrom(1, 2L, 3.0, "4") shouldBe funcToCurried<Int, Long, Double, String>()(1)(2L)(3.0)("4")
    }
    "ex 3.10" {
        fun <A, B, C> curried(f: (A, B) -> C): (A)->(B)->C = { a -> {b -> f(a, b) }}
        curried { a: Int, b: Double -> a * b }(2)(3.6) shouldBe 7.2
    }
    "ex 3.11" {
        val addTax: (Int) -> (Int) -> Int = { x -> { y -> x * 10 + y }}
        fun <A, B, C> swapArgs(f: (A) -> (B) -> C): (B) -> (A) -> C = { b -> { a -> f(a)(b) }}
        addTax(1)(2) shouldBe 12
        swapArgs(addTax)(2)(1) shouldBe  12
    }
})