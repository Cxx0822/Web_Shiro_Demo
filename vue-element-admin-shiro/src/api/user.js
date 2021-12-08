import request from '@/utils/request'

// 登录，参数为 用户对象数据，需包含用户名和密码
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 获取单个用户信息，参数为token
export function getUserRoleInfo(token) {
  return request({
    url: '/user/roleInfo',
    method: 'get',
    // 需要token参数
    params: { token }
  })
}

// 获取所有用户信息，包括用户的角色信息
export function getAllUserRoleInfo() {
  return request({
    url: '/user/allRoleInfo',
    method: 'get'
  })
}

// 获取所有路由信息，供前端过滤和渲染
export function getRoutes() {
  return request({
    url: '/user/routes',
    method: 'get'
  })
}

// 分配用户角色 参数： 用户名 角色名
export function assignUserRoles(username, roleName) {
  return request({
    url: '/user/assignUserRole',
    method: 'post',
    params: { username, roleName }
  })
}

// 新建用户 参数：用户信息
export function createUser(userInfo) {
  return request({
    url: '/user/create',
    method: 'post',
    data: userInfo
  })
}

// 删除用户 参数：用户名
export function deleteUser(username) {
  return request({
    url: '/user/delete',
    method: 'delete',
    params: { username }
  })
}

// 更新用户 参数：用户信息
export function updateUser(userInfo) {
  return request({
    url: '/user/update',
    method: 'post',
    data: userInfo
  })
}

// 查询用户 参数：用户名
export function readUser(username) {
  return request({
    url: '/user/read',
    method: 'get',
    params: { username }
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
