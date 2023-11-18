package edu.iliauni.scheduler.objects

import io.realm.kotlin.types.RealmObject

open class Connection : RealmObject {
    var bluetoothName: String = ""
    var startTime: String =  ""
    var endTime: String = ""
}