const userService = require('../services/user')

const getUser = async ctx => {
  ctx.body = await userService.findById(ctx.params.id)
}

const getAllUsers = async ctx => {
  ctx.body = await userService.getUsers()
}

const createUser = async ctx => {
  ctx.body = await userService.createUser(ctx.request.body)
}

module.exports = {
  getAllUsers,
  createUser,
  getUser,
}
