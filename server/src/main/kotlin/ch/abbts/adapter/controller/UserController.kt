package ch.abbts.adapter.controller

import ch.abbts.application.dto.AuthResponseDto
import ch.abbts.application.dto.SuccessMessage
import ch.abbts.application.dto.UserDto
import ch.abbts.application.dto.UserUpdateDto
import ch.abbts.application.interactor.UserInteractor
import ch.abbts.domain.model.JWebToken
import ch.abbts.error.UserAlreadyExists
import ch.abbts.error.UserNotFound
import ch.abbts.error.WebserverError
import ch.abbts.error.WebserverErrorMessage
import ch.abbts.utils.receiveHandled
import io.github.tabilzad.ktor.annotations.KtorDescription
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["User"])
fun Application.userRoutes(userInteractor: UserInteractor) {
    routing {
        route("/v1/user") {
            @KtorResponds(
                mapping =
                    [
                        ResponseEntry("200", SuccessMessage::class),
                        ResponseEntry("400", WebserverErrorMessage::class),
                        ResponseEntry("500", WebserverErrorMessage::class),
                    ]
            )
            @KtorDescription(
                summary = "Creates a new user in the database.",
                description =
                    """ This API creates a new user if it doesn't already exists.
                When creating, a new user id is given by an integer counter of the db"""",
            )
            post("/register") {
                try {
                    val dto = call.receiveHandled<UserDto>()
                    val newUser = userInteractor.createUser(dto)
                    if (newUser?.id != null) {
                        val token = JWebToken(newUser.email, newUser.id)
                        userInteractor.updateIssuedTime(
                            newUser.email,
                            token.body.iat,
                        )
                        val jwt =
                            JWebToken.generateToken(token.header, token.body)
                        val response =
                            AuthResponseDto(
                                id = newUser.id,
                                jwt = jwt.jwt,
                                email = newUser.email,
                                name = newUser.name,
                                imgUrl = newUser.imageUrl,
                                imgBlob = newUser.imgBase64,
                            )
                        call.respond(HttpStatusCode.OK, response)
                    }
                } catch (e: WebserverError) {
                    val (status, response) =
                        when (e) {
                            is UserAlreadyExists ->
                                HttpStatusCode.Conflict to e.getMessage()

                            else -> e.getStatus() to e.getMessage()
                        }
                    call.respond(status, response)
                }
            }

            authenticate("jwt-auth") {
                @KtorResponds(
                    mapping =
                        [
                            ResponseEntry("200", UserDto::class),
                            ResponseEntry("400", WebserverErrorMessage::class),
                            ResponseEntry("401", WebserverErrorMessage::class),
                            ResponseEntry("500", WebserverErrorMessage::class),
                        ]
                )
                @KtorDescription(
                    summary = "Returns the users profile details",
                    description =
                        """ This API uses the jwt to identify the user.
                If the user exists it returns the users details"""",
                )
                get("/profile") {
                    val userId = JWebToken.getUserIdFromCall(call)
                    val user =
                        userInteractor.getUserById(userId)
                            ?: throw UserNotFound()
                    call.respond(
                        UserDto(
                            email = user.email,
                            name = user.name,
                            zipCode = user.zipCode,
                            birthdate = user.birthdate.toString(),
                            imgBase64 = user.image.toString(),
                            imageUrl = user.imageUrl,
                        )
                    )
                }
            }

            @KtorResponds(
                mapping =
                    [
                        ResponseEntry("200", AuthResponseDto::class),
                        ResponseEntry("400", WebserverErrorMessage::class),
                        ResponseEntry("401", WebserverErrorMessage::class),
                        ResponseEntry("500", WebserverErrorMessage::class),
                    ]
            )
            @KtorDescription(
                summary = "Updates user details",
                description =
                    """ This API uses the jwt to identify the user.
                Then updates the users information based on the request body."""",
            )
            put() {
                val userId = JWebToken.getUserIdFromCall(call)
                val user =
                    userInteractor.getUserById(userId) ?: throw UserNotFound()
                val updateUser = call.receiveHandled<UserUpdateDto>()

                val updateDto =
                    UserDto(
                        email = updateUser.email ?: user.email,
                        name = updateUser.name ?: user.name,
                        password = updateUser.password ?: user.passwordHash,
                        zipCode = updateUser.zipCode ?: user.zipCode,
                        birthdate =
                            updateUser.birthdate ?: user.birthdate.toString(),
                        authProvider = user.authProvider,
                        imgBase64 =
                            updateUser.imgBase64 ?: user.image.toString(),
                    )

                val updatedUserDto =
                    userInteractor.updateUser(user.id!!, updateDto)

                val response =
                    AuthResponseDto(
                        id = updatedUserDto?.id,
                        jwt = "dummy",
                        email = updatedUserDto?.email ?: "",
                        name = updatedUserDto?.name,
                        imgUrl = updatedUserDto?.imageUrl,
                        imgBlob = updatedUserDto?.imgBase64,
                    )
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}
