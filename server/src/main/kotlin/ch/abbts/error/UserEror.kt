package ch.abbts.error

class UserAlreadyExists : WebserverError("user already exists", 400)

class UserCreationFailed(e: Exception) :
    WebserverError("user creation failed with the error \"${e.message}\"", 500)

class UserNotFound() : WebserverError("user not found", 404)

class AuthenticationFailed : WebserverError("Authentication failed", 401)
