package com.harelshaigal.madamal.data.report

data class ReportDto (
    val userId: String = "",
    val title: String = "",
    val data: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    var image: String? = null,
    val lastUpdated: Long? = System.currentTimeMillis()
)

fun ReportDto.toReport(reportId: String): Report {
    return Report(
        id= reportId,
        userId = userId,
        title = title,
        data = data,
        lat = lat,
        lng = lng,
        image = image,
        lastUpdated = lastUpdated
    )
}

fun ReportDto.toMap(): Map<String, Any?> {
    return mapOf(
        "userId" to userId,
        "title" to title,
        "data" to data,
        "lat" to lat,
        "lng" to lng,
        "image" to image,
        "lastUpdated" to lastUpdated
    )
}