package com.harelshaigal.madamal.data

import java.util.Date

fun reportsList(): List<Report> {
    return listOf(
        Report(
            id = 123,
            lat = 1.1,
            lng = 1.1,
            data = "harelush",
            ownerId = 1,
        ),
        Report(
            id = 2,
            lat = 1.1,
            lng = 1.1,
            data = "shaykeaaaa",
            ownerId = 1,
        ),
        Report(
            id = 3,
            lat = 1.1,
            lng = 1.1,
            data = "ddddd",
            ownerId = 1,
        ),
        Report(
            id = 4,
            lat = 1.1,
            lng = 1.1,
            data = "asdsd",
            ownerId = 1,
        ),
        Report(
            id = 5,
            lat = 1.1,
            lng = 1.1,
            data = "dsadsads",
            ownerId = 4,
        ),
    )
}