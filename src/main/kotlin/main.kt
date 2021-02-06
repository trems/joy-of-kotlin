fun main(args: Array<String>) {
	Exercises
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
}






