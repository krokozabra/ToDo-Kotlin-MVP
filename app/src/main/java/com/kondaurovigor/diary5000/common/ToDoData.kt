package com.kondaurovigor.diary5000.common

class ToDoData {
    var id: Int
        set(value) {
            if(value>0)
                field=value
        }
    var name: String
    var day: String
    var month: String
    var year: String
    var description: String
    var ok: Int
    var everyday: Int


    constructor() {
        id = 0
        name = ""
        day = ""
        month = ""
        year = ""
        description = ""
        ok = 0
        everyday = 0
    }

    constructor(
        id: Int,
        name: String,
        day: String,
        month: String,
        year: String,
        description: String,
        OK: Int,
        everyday: Int
    ) {
        this.id = id
        this.name = name
        this.day = day
        this.month = month
        this.year = year
        this.description = description
        ok = OK
        this.everyday = everyday
    }
}