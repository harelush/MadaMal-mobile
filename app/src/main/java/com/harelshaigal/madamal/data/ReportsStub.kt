package com.harelshaigal.madamal.data

import java.util.Date

fun reportsList(): List<Report> {
    return listOf(
        Report(
            id = 1,
            lat = 1.1,
            lng = 1.1,
            data = "ddd1",
            ownerId = 1,
        ),
        Report(
            id = 2,
            lat = 1.1,
            lng = 1.1,
            data = "ddd2",
            ownerId = 1,
        ),
        Report(
            id = 3,
            lat = 1.1,
            lng = 1.1,
            data = "ddd3",
            ownerId = 1,
        ),
        Report(
            id = 4,
            lat = 1.1,
            lng = 1.1,
            data = "ddd4",
            ownerId = 1,
        ),
        Report(
            id = 5,
            lat = 1.1,
            lng = 1.1,
            data = "ddd5",
            ownerId = 4,
        ),
    )
}