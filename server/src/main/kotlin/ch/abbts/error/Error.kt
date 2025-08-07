package ch.abbts.error

class InvalidBody(message: String) : WebserverError(message, 400)

class InvalidQueryParam(param: String) :
    WebserverError("unsupported query param: $param", 400)

class InvalidQueryParamValue(key: String, value: String) :
    WebserverError("unsupported value for $key: $value", 400)

class MissingPathParam(param: String) :
    WebserverError("path param {$param} missing", 400)

open class InvalidPathParam(
    expectedType: String,
    param: String,
    value: String,
) :
    WebserverError(
        "expected $expectedType for $param but $value was given",
        400,
    )

class InvalidPathParamInt(param: String, value: String) :
    InvalidPathParam("int", param, value)
