package edu.iliauni.scheduler.objects

import io.realm.kotlin.types.RealmObject

open class Attendee : RealmObject {
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
    var online: Boolean = false
}