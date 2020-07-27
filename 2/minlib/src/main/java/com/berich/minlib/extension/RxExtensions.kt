package com.berich.minlib.extension

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

inline fun <T> Observable<T>.io_main(): Observable<T> = compose { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
inline fun <T> Observable<T>.io_io(): Observable<T> = compose { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()) }
inline fun <T> Observable<T>.debounceMilliSeconds(timeout: Long = 200): Observable<T> = compose { upstream -> upstream.debounce(timeout, TimeUnit.MILLISECONDS) }