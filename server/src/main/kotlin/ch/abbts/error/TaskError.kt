package ch.abbts.error

class InvalidParams(param: String) :
    WebserverError("unsupported query param: $param", 400)

class InvalidParamValue(key: String, value: String) :
    WebserverError("unsupported value for $key: $value", 400)

class TaskNotFound(id: Int) : WebserverError("task with id $id not found", 204)
