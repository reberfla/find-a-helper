package ch.abbts.error

class InvalidParams(param: String) :
    WebserverError("unsupported query param: $param", 400)

class InvalidParamValue(key: String, value: String) :
    WebserverError("unsupported value for $key: $value", 400)

class MissingPathParam(param: String) :
    WebserverError("path param {$param} missing", 400)

class InvalidPathParam(param: String, value: String) :
    WebserverError("expected Integer for $param but $value was given", 400)

class TaskOfOtherUser() :
    WebserverError("not allowed to update task of other user", 403)

class TaskNotFound(id: Int) : WebserverError("task with id $id not found", 204)
