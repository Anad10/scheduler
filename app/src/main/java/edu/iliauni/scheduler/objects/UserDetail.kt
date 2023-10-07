package edu.iliauni.scheduler.objects

import io.realm.kotlin.types.RealmObject

open class UserDetail : RealmObject {
    var id: Long = 0
    var userName: String = ""
    var userId: Int = 0
    var programCode: String = ""
    var programUrl: String = ""
    var idToken: String = ""
}