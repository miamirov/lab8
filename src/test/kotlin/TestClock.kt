import java.lang.UnsupportedOperationException
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TestClock(private var time: Instant) : Clock() {
    override fun getZone(): ZoneId {
        TODO("Not yet implemented")
    }


    override fun withZone(zone: ZoneId?): Clock {
        TODO("Not yet implemented")
    }

    override fun instant(): Instant {
        return time
    }

    fun addMinutes(inc: Long) {
        time = time.plus(inc, ChronoUnit.MINUTES)
    }
}