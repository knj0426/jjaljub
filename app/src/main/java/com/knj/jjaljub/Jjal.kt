package com.knj.jjaljub

import android.net.Uri
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Jjal : RealmObject() {
    @PrimaryKey
    open var id : Int = 0
    open var path : String? = null
    open var tag : String? = null

}