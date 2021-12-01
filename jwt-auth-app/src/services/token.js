const jwt = require('jsonwebtoken')
const crypto = require('crypto')

const { crypto: config } = require('../../config')
const { Token } = require('../models')

const signOptions = {
  algorithm: 'RS256',
  expiresIn: '30s',
}

const sign = payload => jwt.sign(payload, config.jwt.privateKey, signOptions)

const verify = token => new Promise((resolve, reject) =>
  jwt.verify(
    token,
    config.jwt.publicKey,
    (error, data) => error ? reject(error) : resolve(data)
  ))

const createRefreshToken = async userId => {
  const token = `${userId}${crypto.randomBytes(64).toString('hex')}`
  const expiresAt = new Date(Date.now() + config.refereshToken.duration)
  await Token.create({
    token,
    expiresAt,
    user_id: userId,
    valid: true,
  })
  return { token, expiresAt }
}

const getRefreshToken = token => Token.findOne({ where: { token } })

const invalidateRefreshToken = token => Token.update({ valid: false }, { where: { token } })

const invalidateAllUserRefreshTokens = token => Token.findOne({ where: { token } })
  .then(tokenResult => Token.update({ valid: false }, { where: { user_id: tokenResult.user_id } }))

module.exports = {
  sign,
  verify,
  createRefreshToken,
  getRefreshToken,
  invalidateRefreshToken,
  invalidateAllUserRefreshTokens,
}
