package model

class Logs {


    var logMessage: String = ""
        get() = field
        set(value) {
            field = value
        }

    var logTime: String = ""
        get() = field
        set(value) {
            field = value
        }

    constructor(_logMessage: String, _logTime: String){
        logMessage = _logMessage
        logTime = _logTime
    }
}