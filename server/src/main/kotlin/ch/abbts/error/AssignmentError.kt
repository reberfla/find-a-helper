package ch.abbts.error

class AssignmentNotFound(id: Int) :
    WebserverError("assignment with id $id not found", 204)
