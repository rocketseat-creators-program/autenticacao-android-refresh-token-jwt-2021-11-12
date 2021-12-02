package com.expertsclub.expertsauthentication.ui.login

import android.content.Context
import androidx.lifecycle.*
import com.expertsclub.expertsauthentication.ExpertsApp
import com.expertsclub.expertsauthentication.base.AppCoroutinesDispatchers
import com.expertsclub.expertsauthentication.base.ResultStatus
import com.expertsclub.expertsauthentication.data.repository.AuthRepository
import com.expertsclub.expertsauthentication.data.repository.UserRepository
import com.expertsclub.expertsauthentication.domain.usecase.LoginUseCase
import com.expertsclub.expertsauthentication.framework.network.ApiService
import com.expertsclub.expertsauthentication.framework.network.AuthService
import com.expertsclub.expertsauthentication.framework.network.datasource.ApiRemoteDataSourceImpl
import com.expertsclub.expertsauthentication.framework.network.datasource.AuthRemoteDataSourceImpl
import com.expertsclub.expertsauthentication.framework.network.interceptor.AuthInterceptor
import com.expertsclub.expertsauthentication.framework.network.manager.TokenManagerImpl
import com.expertsclub.expertsauthentication.framework.preferences.datasource.AuthProtoDataSourceImpl
import com.expertsclub.expertsauthentication.framework.preferences.datasource.PreferencesDataSourceImpl
import com.expertsclub.expertsauthentication.framework.preferences.manager.LocalPersistenceManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginStateData = MutableLiveData<LoginState>()
    val loginStateData: LiveData<LoginState> = _loginStateData

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginUseCase.invoke(LoginUseCase.LoginParams(email, password))
                .watchStatus()
        } else _loginStateData.value = LoginState.ShowError
    }

    private fun Flow<ResultStatus<Unit>>.watchStatus() = viewModelScope.launch {
        collect { status ->
            _loginStateData.value = when (status) {
                ResultStatus.Loading -> LoginState.ShowLoading
                is ResultStatus.Success -> LoginState.LoginSuccess
                is ResultStatus.Error -> LoginState.ShowError
            }
        }
    }

    sealed class LoginState {
        object ShowLoading : LoginState()
        object LoginSuccess : LoginState()
        object ShowError : LoginState()
    }

    class LoginViewModelFactory(applicationContext: Context) :
        ViewModelProvider.Factory {

        private val expertsApp = applicationContext as ExpertsApp

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                val authDataSource = AuthProtoDataSourceImpl(expertsApp.authDataStore)
                val localDataSource = PreferencesDataSourceImpl(expertsApp.localDataStore)
                val tokenManager = TokenManagerImpl(authDataSource)
                val localPersistenceManager = LocalPersistenceManagerImpl(localDataSource)
                val authRemoteDataSource = AuthRemoteDataSourceImpl(AuthService.getService())
                val authInterceptor = AuthInterceptor(tokenManager, authRemoteDataSource)
                val remoteDataSource =
                    ApiRemoteDataSourceImpl(ApiService.getService(authInterceptor))
                val authRepository = AuthRepository(authRemoteDataSource, tokenManager)
                val dispatchers = AppCoroutinesDispatchers(
                    io = Dispatchers.IO,
                    computation = Dispatchers.Default,
                    main = Dispatchers.Main
                )
                val userRepository = UserRepository(remoteDataSource, localPersistenceManager)

                val loginUseCase = LoginUseCase(authRepository, userRepository, dispatchers)

                return LoginViewModel(loginUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}