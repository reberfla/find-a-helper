package application.dto

interface DTO <T> {

    fun toModel():T

    companion object{}
}
