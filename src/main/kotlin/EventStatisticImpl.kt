import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit

import java.util.HashMap




class EventStatisticImpl(private val clock: Clock) : EventsStatistic {
    private val events: MutableMap<String, MutableList<Instant>> = mutableMapOf()
    override fun incEvent(name: String) {
        events.getOrPut(name) { mutableListOf() }.add(clock.instant())
    }

    override fun getEventStatisticByName(name: String): Double {
        return statisticsByLastHour().getOrDefault(name, mutableListOf()).size / MINUTES
    }

    override fun allEventStatistic(): Map<String, Double> {
        val lastHour = statisticsByLastHour()
        val result: MutableMap<String, Double> = mutableMapOf()
        for (name in events.keys) {
            result[name] = lastHour.getOrDefault(name, mutableListOf()).size / MINUTES
        }
        return result
    }

    override fun printStatistic() {
        val statistic: MutableMap<Instant, MutableList<String>> = HashMap()
        events.forEach { (name, listOfTimes) ->
            for (time in listOfTimes) {
                statistic.getOrPut(time) { mutableListOf() }.add(name)
            }
        }
        statistic.forEach { (time, name) ->
            println("$time:${name.joinToString(", ")}")
        }
    }

    private fun statisticsByLastHour(): Map<String, List<Instant>> {
        val prev = clock.instant().minus(1L, ChronoUnit.HOURS)
        val result: MutableMap<String, MutableList<Instant>> = mutableMapOf()
        events.forEach { (key, value) ->
            result[key] = value.filter { it.isAfter(prev) }.toMutableList()
        }
        return result
    }

    companion object {
        private const val MINUTES = 60.0
    }
}