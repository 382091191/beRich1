package com.berich.minlib.utils

import android.content.Context
import android.text.TextUtils
import android.util.ArrayMap
import com.berich.minlib.R
import com.berich.minlib.consts.CommonTime
import com.berich.minlib.extension.day
import com.berich.minlib.extension.hour
import com.berich.minlib.extension.minute
import com.berich.minlib.extension.second
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * 日期处理工具类
 */
object TimeUtil {

    const val TIME_HM = "${CommonTime.HOUR}:${CommonTime.MINUTE}"
    const val TIME_HMS = "${CommonTime.HOUR}:${CommonTime.MINUTE}:${CommonTime.SECOND}"
    const val DATE_MD = "${CommonTime.MONTH}-${CommonTime.DAY}"
    const val DATE_YMD = "${CommonTime.YEAR}-${CommonTime.MONTH}-${CommonTime.DAY}"
    const val DATETIME_MDHM = "${CommonTime.MONTH}-${CommonTime.DAY} ${CommonTime.HOUR}:${CommonTime.MINUTE}"
    const val DATETIME_ALL = "${CommonTime.YEAR}-${CommonTime.MONTH}-${CommonTime.DAY} ${CommonTime.HOUR}:${CommonTime.MINUTE}:${CommonTime.SECOND}"

    var locale = Locale("en")
        set(value) {
            field = value
            clearFormat()
        }

    private val SDF_MAP = ArrayMap<String, SimpleDateFormat>()

    private fun clearFormat() {
        SDF_MAP.clear()
    }

    fun getTimeStr(context: Context, time: Long): String {
        var format = SimpleDateFormat("yyyy-MM-dd")
        var t = Date()
        t.time = time
        if (isSameDay(time, System.currentTimeMillis())) {
            return format.format(t) + context.getString(R.string.today)
        }
        if (isSameDay(time, (System.currentTimeMillis() - 1.day()))) {
            return format.format(t) + context.getString(R.string.yestoday)
        }

        return format.format(t)
    }

    fun getSimpleDateFormat(pattern: String): SimpleDateFormat {
        var format = SDF_MAP[pattern]
        if (format == null) {
            format = SimpleDateFormat(pattern, locale)
            SDF_MAP[pattern] = format
        }
        return format
    }

    /**
     * 当前时间
     */
    fun getCurrTime(pattern: String): String {
        return getSimpleDateFormat(pattern).format(Date())
    }

    /**
     * long to str
     */
    fun secLong2Str(time: Long, pattern: String = DATETIME_ALL): String {
        return milliSecLong2Str(time * 1000, pattern)
    }

    /**
     * long to str
     */
    fun milliSecLong2Str(time: Long, pattern: String = DATETIME_ALL): String {
        return getSimpleDateFormat(pattern).format(Date(time))
    }

    /**
     * str to long
     */
    fun str2Long(dateStr: String, pattern: String): Long {
        return if (TextUtils.isEmpty(dateStr)) 0 else str2Date(dateStr, pattern).time
    }

    /**
     * date to str
     */
    fun date2Str(date: Date, pattern: String): String {
        return getSimpleDateFormat(pattern).format(date)
    }

    /**
     * str to date
     */
    fun str2Date(str: String, pattern: String): Date {
        return getSimpleDateFormat(pattern).parse(str)
    }

    /**
     * 获取年份
     */
    fun getYear(dateStr: String): String {
        return date2Str(str2Date(dateStr, CommonTime.YEAR), CommonTime.YEAR)
    }

    /**
     * 获取月份
     */
    fun getMonth(dateStr: String): String {
        return date2Str(str2Date(dateStr, CommonTime.MONTH), CommonTime.MONTH)
    }

    /**
     * 获取第几周
     */
    fun getWeek(dateStr: String, pattern: String): Int {
        val date = str2Date(dateStr, pattern)
        val c = calendar
        c.time = date
        return c[Calendar.WEEK_OF_MONTH]
    }

    /**
     * 获取第几天
     */
    fun getDay(dateStr: String): String {
        return date2Str(str2Date(dateStr, CommonTime.DAY), CommonTime.DAY)
    }

    /**
     * 当前月份有多少天
     */
    fun getDaysInCrtMonth(year: Int, month: Int): Int {
        val mArray = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        // 判断闰年的情况 ，2月份有29天；
        if (year % 400 == 0 || year % 100 != 0 && year % 4 == 0) {
            mArray[1] = 29
        }
        return mArray[month - 1]
    }

    /**
     * 返回指定这个月的第几天
     */
    fun getDayOfMonth(year: Int, month: Int, weekOfMonth: Int, dayOfWeek: Int): Int { // 在具有默认语言环境的默认时区内使用当前时间构造一个默认的 GregorianCalendar。
        val cal = calendar
        // 不保留以前的设置
        cal.clear()
        // 将日期设置为本月的第一天。
        cal[year, month - 1] = 1
        cal[Calendar.DAY_OF_WEEK_IN_MONTH] = weekOfMonth
        cal[Calendar.DAY_OF_WEEK] = dayOfWeek
        // WEEK_OF_MONTH表示当天在本月的第几个周。不管1号是星期几，都表示在本月的第一个周
        return cal[Calendar.DAY_OF_MONTH]
    }

    /**
     * 取得给定日期加上一定天数后的日期对象.
     */
    fun dateAddDay(date: Date, amount: Int): Date {
        val cal = calendar
        cal.time = date
        cal.add(GregorianCalendar.DATE, amount)
        return cal.time
    }

    /**
     * 返回当前时间加实数小时后的日期时间。
     */
    fun dateAddHour(hour: Float): Date {
        val addMinute = (hour * 60).toInt()
        val cal = calendar
        cal.add(GregorianCalendar.MINUTE, addMinute)
        return cal.time
    }

    /**
     * 计算月份差
     */
    fun monthDiff(startDate: Date, endDate: Date): Int {
        val startCalendar = calendar
        startCalendar.time = startDate
        val endCalendar = calendar
        endCalendar.time = endDate
        val startYear = startCalendar[Calendar.YEAR]
        val startMonth = startCalendar[Calendar.MONTH]
        val endYear = endCalendar[Calendar.YEAR]
        val endMonth = endCalendar[Calendar.MONTH]
        return (endYear - startYear) * 12 + (endMonth - startMonth)
    }

    /**
     * 计算天数差
     */
    fun dayDiff(startDate: Date, endDate: Date): Long {
        val difference = endDate.time - startDate.time
        return difference / 1.day()
    }

    fun isSameDay(time1: Long, time2: Long): Boolean {
        var cal1 = Calendar.getInstance();
        cal1.timeInMillis = time1
        var cal2 = Calendar.getInstance();
        cal2.timeInMillis = time2
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 计算小时差
     */
    fun hourDiff(startDate: Date, endDate: Date): Long {
        val difference = endDate.time - startDate.time
        return difference / 1.hour()
    }

    /**
     * 获取相对时间
     */
    fun getRelativeTimeString(timestamp: Long): String {
        val oldCalendar = calendar
        oldCalendar.timeInMillis = timestamp
        val crtCalendar = calendar
        val current = crtCalendar.timeInMillis
        val diff = current - timestamp
        if (diff < 1.minute()) {
            return "刚刚"
        } else if (diff < 1.hour()) {
            return String.format("%d 分钟前", diff / 1.second()) // 显示:1分钟前 - 60 分钟前
        }
        return if (oldCalendar[Calendar.YEAR] == crtCalendar[Calendar.YEAR]) {
            when {
                oldCalendar[Calendar.DAY_OF_YEAR] == crtCalendar[Calendar.DAY_OF_YEAR] -> { // 当天
                    String.format("今天 %s", getSimpleDateFormat(TIME_HM).format(oldCalendar.time)) // 显示： 时分
                }
                oldCalendar[Calendar.DAY_OF_YEAR] == (crtCalendar[Calendar.DAY_OF_YEAR] - 1) -> {
                    String.format("昨天 %s", getSimpleDateFormat(TIME_HM).format(oldCalendar.time)) // 显示：昨天 时分
                }
                else -> {
                    getSimpleDateFormat(DATETIME_MDHM).format(oldCalendar.time) // 显示：月日 时分
                }
            }
        } else {
            getSimpleDateFormat(DATE_YMD).format(oldCalendar.time) // 显示：年月日
        }
    }

    private val timeZone: TimeZone
        get() = TimeZone.getTimeZone("GMT+8")

    private val calendar: Calendar
        get() {
            return GregorianCalendar.getInstance()
        }

    fun setCalendarZero(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    fun long2Calendar(timestamp: Long): Calendar {
        val calendar = calendar
        calendar.timeInMillis = timestamp * 1000
        return calendar
    }
}

fun Long.isBefore(): Boolean {
    val calendar = Calendar.getInstance()
    val currentCalendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return currentCalendar.after(calendar)
}

