package com.rdua.whiteboard.common.time

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Manages timestamps. Timestamp created taking local time-zone into consideration.
 */
class Timestamp private constructor(
    private val time: ZonedDateTime,
) {
    /**
     * Converts current [Timestamp] to [Long].
     */
    fun toEpochSecond(): Long = time.toEpochSecond()

    /**
     * Converts current [Timestamp] to [Long] milliseconds after Epoch.
     */
    fun toEpochMilli(): Long = time.toInstant().toEpochMilli()

    /**
     * Formats this [Timestamp] using the specified [formatter].
     */
    fun toFormattedString(
        formatter: DateTimeFormatter = defaultFormatter(),
    ): String {
        return time.format(formatter)
    }

    /**
     * Checks if this [Timestamp] is more recent than [other].
     */
    fun isBefore(other: Timestamp): Boolean = this.time.isBefore(other.time)

    /**
     * Checks if this [Timestamp] was made after [other].
     */
    fun isAfter(other: Timestamp): Boolean = this.time.isAfter(other.time)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Timestamp

        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        return time.hashCode()
    }

    override fun toString() = time.toString()

    companion object {
        /**
         * Creates a new [Timestamp] with local time-zone.
         */
        fun now(): Timestamp = Timestamp(ZonedDateTime.now())

        /**
         * Converts [epochSeconds] to [Timestamp].
         *
         * @throws Exception if the instant exceeds the maximum or minimum instant.
         */
        fun fromEpochSecond(
            epochSeconds: Long,
            zoneId: ZoneId = Clock.systemDefaultZone().zone,
        ): Timestamp {
            val instant = Instant.ofEpochSecond(epochSeconds)
            return Timestamp(ZonedDateTime.ofInstant(instant, zoneId))
        }

        /**
         * Converts [epochMilli] to [Timestamp].
         *
         * @throws Exception if the instant exceeds the maximum or minimum instant.
         */
        fun fromEpochMilli(
            epochMilli: Long,
            zoneId: ZoneId = Clock.systemDefaultZone().zone,
        ): Timestamp {
            val instant = Instant.ofEpochMilli(epochMilli)
            return Timestamp(ZonedDateTime.ofInstant(instant, zoneId))
        }

        /**
         * Converts [epochSeconds] to [Timestamp] and formats it using [formatter].
         *
         * @throws Exception if the instant exceeds the maximum or minimum instant.
         */
        fun toFormattedString(
            epochSeconds: Long,
            formatter: DateTimeFormatter = defaultFormatter(),
            zoneId: ZoneId = Clock.systemDefaultZone().zone,
        ): String {
            val instant = Instant.ofEpochSecond(epochSeconds)
            return ZonedDateTime.ofInstant(instant, zoneId).format(formatter)
        }

        private fun defaultFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.getDefault())
    }
}