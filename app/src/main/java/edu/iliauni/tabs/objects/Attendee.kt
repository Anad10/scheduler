package edu.iliauni.tabs.objects

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


open class Attendee : RealmObject {
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
    var online: Boolean = false
}