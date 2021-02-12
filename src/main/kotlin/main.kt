fun main(args: Array<String>) {
	testables.forEach { it.invokeTest() }
}
val testables = mutableListOf<Testable>()
abstract class Testable {
	init { testables.add(this) }
	abstract fun test(): Boolean
	fun invokeTest() = require(test())
	override fun toString(): String {
		return this.javaClass.name
	}
}
object Exercises {
	val square: (Int) -> Long = { it * 2L }
	val triple: (Double) -> Int = { (it * 3).toInt() }
	val ex_3_2 = object : Testable() {
		fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { f(g(it)) }
		override fun test() = compose<String, Int, Int>({ x -> x * 2 }, { x -> x.length % 2})("kotlin") == 0
	}
	val ex_3_3 = object : Testable() {
		val add: (Int) -> (Int) -> Int = { a -> { b -> a + b }}
		override fun test(): Boolean = add(3)(2) == 5
	}
	val ex_3_4 = object : Testable() {
		val compose: ((Int) -> Long) -> ((Double) -> Int) -> (Double) -> Long =
			{ x -> { y -> { z -> x(y(z)) }}}
		override fun test(): Boolean {
			val squareOfTriple: (Double) -> Long = compose(square)(triple)
			return squareOfTriple(2.0) == 12L
		}
	}
	val ex_3_5 = object : Testable() {
		fun <T, U, V> higherCompose(): ((U) -> V) -> ((T) -> U) -> (T) -> V =
			{ x: (U) -> V -> { y: (T) -> U -> { z: T -> x(y(z)) }} }
		override fun test(): Boolean {
			val squareOfTriple: (Double) -> Long = higherCompose<Double, Int, Long>()(square)(triple)
			return squareOfTriple(2.0) == 12L
		}
	}
	val ex_3_6 = object : Testable() {
		fun <T, U, V> higherAndThen() =
			{ x: (T) -> U -> { y: (U) -> V -> { z: T -> y(x(z)) }}}
		override fun test(): Boolean {
			val squareOfTriple: (Double) -> Long = higherAndThen<Double, Int, Long>()(triple)(square)
			return squareOfTriple(2.0) == 12L
		}
	}
	val ex_3_7 = object : Testable() {
		fun <A, B, C> partially(a: A, f: (A) -> (B) -> C): (B) -> C = f(a)
		override fun test(): Boolean {
			val f: (Long) -> String = partially<Int, Long, String>(2) { a: Int -> { l: Long -> (l + a).toString() } }
			return f(3L) == "5"
		}
	}
	val ex_3_8 = object : Testable() {
		fun <A, B, C> partial(b: B, f: (A) -> (B) -> C): (A) -> C = { a: A -> f(a)(b) }
		override fun test(): Boolean {
			val f = partial<Int, Long, String>(10L, { i: Int -> { l: Long -> (l * i).toString() } })
			return f(2) == "20"
		}
	}
	val ex_3_9 = object : Testable() {
		fun <A, B, C, D> funcFrom(a: A, b: B, c: C, d: D): String = "$a $b $c $d"
		fun <A, B, C, D> funcToCurried(): (A) -> (B) -> (C) -> (D) -> String = {a -> {b -> {c -> {d -> "$a $b $c $d" }}}}
		override fun test(): Boolean {
			return funcFrom(1, 2L, 3.0, "4") == funcToCurried<Int, Long, Double, String>()(1)(2L)(3.0)("4")
		}
	}
	val ex_3_10 = object : Testable() {
		fun <A, B, C> curried(f: (A, B) -> C): (A)->(B)->C = { a -> {b -> f(a, b) }}
		override fun test(): Boolean = curried { a: Int, b: Double -> a + b }(2)(3.6) == 7.2
	}
	val ex_3_11 = object : Testable() {
		val addTax: (Int) -> (Int) -> Int = { x -> { y -> x * 10 + y }}
		fun <A, B, C> swapArgs(f: (A) -> (B) -> C): (B) -> (A) -> C = { b -> { a -> f(a)(b) }}
		override fun test(): Boolean = addTax(1)(2) == 12 && swapArgs(addTax)(2)(1) == 12
	}
	val ex_4_1 = object : Testable() {
		fun inc(n: Int) = n + 1
		fun dec(n: Int) = n - 1
		tailrec fun add(a: Int, b: Int): Int = if (b == 0) a else add(inc(a), dec(b))
		override fun test(): Boolean = add(3, 7) == 10
	}
}






