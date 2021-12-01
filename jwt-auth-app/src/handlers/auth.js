const auth = require('../services/auth')

const authenticate = async ctx => {
  const { email, password } = ctx.request.body
  const { accessToken, refreshToken, refreshTokenExpiration } = await auth.authenticate({ email, password })
  ctx.body = {
    accessToken,
    refreshToken,
    expires: refreshTokenExpiration,
  }
}

const refreshToken = async ctx => {
  const { refreshToken } = ctx.request.body
  const { accessTokenValue, refreshTokenValue, refreshTokenExpiration } = await auth.refreshToken(refreshToken)
  ctx.body = {
    accessToken: accessTokenValue,
    refreshToken: refreshTokenValue,
    expires: refreshTokenExpiration,
  }
}

const logout = async ctx => {
  const { refreshToken, allDevices } = ctx.request.body
  await auth.logout({ refreshTokenValue: refreshToken, allDevices })
  ctx.body = {}
}

module.exports = {
  authenticate,
  refreshToken,
  logout,
}
