/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/
package com.ninja.browser.free.video.downloader.base_screen

import com.google.gson.Gson
import com.ninja.browser.free.video.downloader.core.ApiService
import com.ninja.browser.free.video.downloader.utils.RetrofitUtils
import com.x.common.utils.AESUtils
import com.x.common.utils.encrypt.MD5Util
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap


open class BaseRepository : MvpRepository {
	//密钥:74kr6Bi0PSDJR6XN
	//key:74kr6Bi0PSDJR6XN
	var apiService = RetrofitUtils.getInstance()?.createApi(ApiService::class.java)
	private var gson = Gson()

	private fun createQ(json: String): String {
		return AESUtils.encrypt(json)
	}

	private fun createS(map: Map<String, String>): String {
		var keyList = map.keys.toList()
		Collections.sort(keyList, String.CASE_INSENSITIVE_ORDER)
		var result = ""
		keyList.forEach {
			result += "$it=${map[it]}&"
		}
		result += "key=74kr6Bi0PSDJR6XN"
		return MD5Util.encode(result)
	}

	open fun createRequestBody(map: Map<String, String>): RequestBody {
		val json: String = gson.toJson(map)
		var body ="q=${URLEncoder.encode(createQ(json),"UTF-8")}&sign=${createS(map)}"//需要做 url转义 否则部分符号会丢失
		return body.toRequestBody("application/x-www-form-urlencoded".toMediaType())
	}

	public inner class ParamsBuilder {
		private var params = HashMap<String, String>()
		fun put(key: String, value: String): ParamsBuilder {
			params[key] = value
			return this
		}

		fun build(): RequestBody {
			params["timestamp"] = "${System.currentTimeMillis() / 1000}"
			return createRequestBody(params)
		}
	}
}
