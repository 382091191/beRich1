package com.berich.minlib.extension

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


fun File.read(): String {
    if (!exists()) return ""
    var inputStream: FileInputStream? = null
    try {
        inputStream = FileInputStream(path)
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)
        return String(bytes)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }
    return ""
}