package ch.abbts.error

class TaskOfOtherUser() :
    WebserverError("not allowed to update task of other user", 403)

class TaskNotFound(id: Int) : WebserverError("task with id $id not found", 204)
