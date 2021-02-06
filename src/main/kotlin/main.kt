fun main(args: Array<String>) {
	require(Ex_3_2.compose<String, Int, Int>({ x -> x * 2 }, { x -> x.length % 2})("kotlin") == 0)
	require(Ex_3_3.add(3)(2) == 5)
}
object Ex_3_2 {
	fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { f(g(it)) }
}
object Ex_3_3 {
	val add: (Int) -> (Int) -> Int = { a -> { b -> a + b }}
}


