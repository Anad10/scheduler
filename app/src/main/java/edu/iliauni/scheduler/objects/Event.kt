package edu.iliauni.scheduler.objects

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

open class Event: RealmObject {
    var id: Long = 0
    var name: String = ""
    var startDates: RealmList<String> = realmListOf()
    var endDates: RealmList<String> = realmListOf()
    var attendees: RealmList<Attendee>? = realmListOf()
    var description: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var host: Host? = null
}