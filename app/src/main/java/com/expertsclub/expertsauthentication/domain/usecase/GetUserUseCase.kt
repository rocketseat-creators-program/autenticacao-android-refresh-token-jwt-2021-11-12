package com.expertsclub.expertsauthentication.domain.usecase

import com.expertsclub.expertsauthentication.base.AppCoroutinesDispatchers
import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.base.ResultUseCase
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.domain.model.User
import com.expertsclub.expertsauthentication.framework.network.response.toUserDomain
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutinesDispatchers
) : ResultUseCase<Unit, User>() {

    override suspend fun doWork(params: Unit): ResultStatus<User> {
        return withContext(dispatchers.io) {
            ResultStatus.Success(userRepository.getUser().toUserDomain())
        }
    }
}