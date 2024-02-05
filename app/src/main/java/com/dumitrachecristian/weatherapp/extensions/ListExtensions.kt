package com.dumitrachecristian.weatherapp.extensions

fun List<String>.findMostFrequentString(): String? {
    if (isEmpty()) return null

    val stringFrequencyMap = mutableMapOf<String, Int>()

    for (string in this) {
        val currentCount = stringFrequencyMap.getOrDefault(string, 0)
        stringFrequencyMap[string] = currentCount + 1
    }

    return stringFrequencyMap.maxByOrNull { it.value }?.key
}