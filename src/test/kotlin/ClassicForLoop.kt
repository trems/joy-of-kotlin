import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.system.measureNanoTime

fun <T> forLoop(
	start: T,
	cond: (T) -> Boolean = { true },
	step: (T) -> T = { it },
	body: (T) -> Unit
) {
    tailrec fun process(counter: T) {
        if (cond(counter)) {
            body(counter)
            process(step(counter))
        }
    }
    process(start)
}

inline fun <T> forLoopInline(
	start: T,
	cond: (T) -> Boolean = { true },
	step: (T) -> T = { it },
	body: (T) -> Unit
) {
    var counter = start
    while (cond(counter)) {
        body(counter)
        counter = step(counter)
    }
}

class ForLoopTest : FunSpec() {

    init {
        context("inline vs noinline forLoop") {
            val inlinedMeasurements = mutableListOf<Long>()
            val notInlinedMeasurements = mutableListOf<Long>()
            test("ints inlined").config(invocations = 10_000) {
                val ints = mutableListOf<Int>()
                inlinedMeasurements += measureNanoTime {
                    forLoopInline(0, { it < 10 }, { it + 2 }) {
                        ints += it
                    }
                }
                ints shouldBe listOf(0, 2, 4, 6, 8)
            }
            test("ints").config(invocations = 10_000) {
                val ints = mutableListOf<Int>()
                notInlinedMeasurements += measureNanoTime {
                    forLoop(0, { it < 10 }, { it + 2 }) {
                        ints += it
                    }
                }
                ints shouldBe listOf(0, 2, 4, 6, 8)
            }

            println("Inline while-based avg: ${inlinedMeasurements.average()} ns")
            println("Not inline tailrec-based avg: ${notInlinedMeasurements.average()} ns")
        }


        test("iterator") {
            val list = listOf(0, 2, 4, 6, 8)
            val res = mutableListOf<Int>()
            forLoop(list.iterator(), { it.hasNext() }) {
                res += it.next() * 2
            }
            res shouldBe listOf(0, 4, 8, 12, 16)
        }

        test("nested loops (bubble sort lol)") {
            val array = intArrayOf(3, 6, 1, 9, 3, 78, 4, 3, 6, 9)
            val expected = array.sortedArray()
            forLoopInline(0, { it < array.size }, { it + 1 }) { i ->
                forLoop(0, { it < array.size }, { it + 1 }) { j ->
                    if (array[i] < array[j]) {
                        val tmp = array[i]
                        array[i] = array[j]
                        array[j] = tmp
                        println(array.joinToString())
                    }
                }
            }
            array shouldBe expected
        }
    }
}