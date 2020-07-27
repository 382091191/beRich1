package com.berich.minlib.extension

inline fun Int.second() = this * 1000L
inline fun Int.minute() = this * 1000L * 60
inline fun Int.hour() = this * 1000L * 60 * 60
inline fun Int.day() = this * 1000L * 24 * 60 * 60
inline fun Int.month() = this * 1000L * 24 * 60 * 60 * 30
inline fun Int.year() = this * 1000L * 24 * 60 * 60 * 30 * 12