package ch.abbts.error

class OfferForbidden : WebserverError("You are not allowed to make this offer.", 403)
class OfferCreationFailed : WebserverError("Failed to create the offer due to server error.", 500)
class OfferAlreadyExists : WebserverError("User has already submitted an offer for this task.", 409)
class OfferDeletionNotAllowed : WebserverError("Offer cannot be deleted after it has been accepted.", 403)
class OfferNotFound : WebserverError("Offer not found", 404)
