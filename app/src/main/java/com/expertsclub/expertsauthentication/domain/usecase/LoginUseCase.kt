package com.expertsclub.expertsauthentication.domain.usecase

import com.expertsclub.expertsauthentication.base.AppCoroutinesDispatchers
import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.base.ResultUseCase
import com.expertsclub.expertsauthentication.data.repository.AuthRepository
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.domain.model.TokenData
import com.expertsclub.expertsauthentication.framework.extension.getUserIdFromAccessToken
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutinesDispatchers
) : ResultUseCase<LoginUseCase.LoginParams, Unit>() {

    override suspend fun doWork(params: LoginParams): ResultStatus<Unit> {
        return withContext(dispatchers.io) {
            val authResponse = authRepository.login(params.email, params.password)
            val tokenData = TokenData(authResponse.accessToken, authResponse.refreshToken)
            authRepository.saveAccessToken(tokenData)
            userRepository.saveUserId(authResponse.getUserIdFromAccessToken())
            ResultStatus.Success(Unit)
        }
    }

    data class LoginParams(val email: String, val password: String)
}