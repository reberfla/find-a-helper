
import ch.abbts.adapter.database.repository.usersRepository
import org.gradle.internal.impldep.com.jcraft.jsch.jbcrypt.BCrypt

class usersInteractor(
    private val userRepository: usersRepository,
) {

    fun createLocalUser(dto: usersDto): Boolean {
        val existing = authenticateLocalUser(dto)
        if (existing){
            return false
        }

        val hashed = dto.password_hash?.let { BCrypt.hashpw(it, BCrypt.gensalt()) } ?: return false
        val newUser = dto.copy(password_hash = hashed).toModel()
        userRepository.createLocalUser(newUser)
        return true
    }

    fun authenticateLocalUser(user: usersDto): Boolean {
        return userRepository.authenticateLocalUser(user.toModel())
    }

}
