package com.harelshaigal.madamal.data

import java.util.Date

fun reportsList(): List<Report> {
    return listOf(
        Report(
            id = 1,
            creationDate = Date(),
            data = "ddd1",
            ownerId = 1,
        ),
        Report(
            id = 2,
            creationDate = Date(),
            data = "ddd2",
            ownerId = 1,
        ),
        Report(
            id = 3,
            creationDate = Date(),
            data = "ddd3",
            ownerId = 1,
        ),
        Report(
            id = 4,
            creationDate = Date(),
            data = "ddd4",
            ownerId = 1,
        ),
        Report(
            id = 5,
            creationDate = Date(),
            data = "ddd5",
            ownerId = 4,
        ),
    )
}