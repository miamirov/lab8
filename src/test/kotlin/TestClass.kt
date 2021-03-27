import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class TestClass {
    private lateinit var clock: TestClock
    private lateinit var eventsStatistic: EventsStatistic


    @BeforeEach
    fun init() {
        clock = TestClock(Instant.now())
        eventsStatistic = EventStatisticImpl(clock)
    }

    @Test
    fun testSimpleGet() {
        eventsStatistic.incEvent("test1")
        eventsStatistic.incEvent("test2")
        eventsStatistic.incEvent("test1")
        Assertions.assertEquals(0.03333333333333333, eventsStatistic.getEventStatisticByName("test1"), EPS)
        Assertions.assertEquals(0.01666666666666666, eventsStatistic.getEventStatisticByName("test2"), EPS)
    }

    @Test
    fun testAllEventsSimple() {
        eventsStatistic.incEvent("test1")
        eventsStatistic.incEvent("test2")
        eventsStatistic.incEvent("test1")
        val result: MutableMap<String, Double> = mutableMapOf()
        result["test1"] = 0.03333333333333333
        result["test2"] = 0.01666666666666666
        assertMapsEquals(result, eventsStatistic.allEventStatistic())
        clock.addMinutes(120)
        eventsStatistic.incEvent("test1")
        eventsStatistic.incEvent("test2")
        eventsStatistic.incEvent("test3")
        result.clear()
        result["test1"] = 0.01666666666666666
        result["test2"] = 0.01666666666666666
        result["test3"] = 0.01666666666666666
        assertMapsEquals(result, eventsStatistic.allEventStatistic())

    }

    @Test
    fun testAllEventsConseque() {
        for (i in 1..30) {
            eventsStatistic.incEvent("test1")
            eventsStatistic.incEvent("test2")
            eventsStatistic.incEvent("test3")
        }
        val result: MutableMap<String, Double> = mutableMapOf()


        result["test1"] = 0.5
        result["test2"] = 0.5
        result["test3"] = 0.5
        assertMapsEquals(result, eventsStatistic.allEventStatistic())

        clock.addMinutes(30)

        for (i in 1..30) {
            eventsStatistic.incEvent("test1")
            eventsStatistic.incEvent("test2")
            eventsStatistic.incEvent("test3")
        }

        result["test1"] = 1.0
        result["test2"] = 1.0
        result["test3"] = 1.0
        assertMapsEquals(result, eventsStatistic.allEventStatistic())

        clock.addMinutes(30)
        for (i in 1..30) {
            eventsStatistic.incEvent("test1")
            eventsStatistic.incEvent("test2")
            eventsStatistic.incEvent("test3")
        }

        result["test1"] = 1.0
        result["test2"] = 1.0
        result["test3"] = 1.0
        assertMapsEquals(result, eventsStatistic.allEventStatistic())
    }


    companion object {
        private const val EPS = 1e-8

        private fun assertMapsEquals(expected: Map<String, Double>, actual: Map<String, Double>) {
            Assertions.assertEquals(expected.size, actual.size)
            for (name in expected.keys) {
                assert(actual.containsKey(name))
                Assertions.assertEquals(expected[name]!!, actual[name]!!, EPS)
            }
        }

    }
}