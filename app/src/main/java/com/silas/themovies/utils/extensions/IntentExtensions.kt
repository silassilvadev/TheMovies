package com.silas.themovies.utils.extensions

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.lang.ClassCastException

fun Intent.setupParams(vararg params: Pair<String, Any>) {
    params.forEach {
        when (val param = it.second) {
            is Int -> putExtra(it.first, param)
            is Long -> putExtra(it.first, param)
            is CharSequence -> putExtra(it.first, param)
            is String -> putExtra(it.first, param)
            is Float -> putExtra(it.first, param)
            is Double -> putExtra(it.first, param)
            is Char -> putExtra(it.first, param)
            is Short -> putExtra(it.first, param)
            is Boolean -> putExtra(it.first, param)
            is Serializable -> putExtra(it.first, param)
            is Bundle -> putExtra(it.first, param)
            is Parcelable -> putExtra(it.first, param)
            is Array<*> -> when {
                param.isArrayOf<CharSequence>() -> putExtra(it.first, param)
                param.isArrayOf<String>() -> putExtra(it.first, param)
                param.isArrayOf<Parcelable>() -> putExtra(it.first, param)
                else -> throw ClassCastException("Intent extra ${it.first} has wrong type ${param.javaClass.name}")
            }
            is IntArray -> putExtra(it.first, param)
            is LongArray -> putExtra(it.first, param)
            is FloatArray -> putExtra(it.first, param)
            is DoubleArray -> putExtra(it.first, param)
            is CharArray -> putExtra(it.first, param)
            is ShortArray -> putExtra(it.first, param)
            is BooleanArray -> putExtra(it.first, param)
            else -> throw ClassCastException("Intent extra ${it.first} has wrong type ${param.javaClass.name}")
        }
    }
}