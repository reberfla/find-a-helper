package ch.abbts.error

class AssignmentNotFound(id: Int) :
    WebserverError("assignment with id $id not found", 204)

class AssignmentUpdateNotAllowed() :
    WebserverError(
        "Update of assigment not allowed as the user is not the task creator",
        403,
    )
