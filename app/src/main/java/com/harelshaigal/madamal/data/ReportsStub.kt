
import com.harelshaigal.madamal.data.Report

fun reportsList(): List<Report> {
    return listOf(
        Report(
            id = 1,
            lat = 31.97007377827919,
            lng = 35.0,
            data = "ddd1",
            title = "Title",
            userId = "123",
        ),
        Report(
            id = 2,
            lat = 32.0,
            lng = 34.87287831388922,
            data = "ddd2",
            title = "Title",
            userId = "123",
        ),
        Report(
            id = 3,
            lat = 31.97027377827919,
            lng = 34.92878313889215,
            data = "ddd3",
            title = "Title",
            userId = "123",
        ),
        Report(
            id = 4,
            lat = 31.97307377827919,
            lng = 34.77284313889215,
            data = "ddd4",
            title = "Title",
            userId = "123",
        ),
        Report(
            id = 5,
            lat = 31.97707377827919,
            lng = 35.1,
            data = "ddd5",
            title = "Title",
            userId = "123",
        ),
    )
}