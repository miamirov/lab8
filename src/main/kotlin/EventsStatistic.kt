interface EventsStatistic {
    fun incEvent(name: String)
    fun getEventStatisticByName(name: String): Double
    fun allEventStatistic(): Map<String, Double>
    fun printStatistic()
}