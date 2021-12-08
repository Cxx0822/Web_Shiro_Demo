import request from '@/utils/request'

// 获取所有的角色信息，包括角色的权限信息
export function getAllRoles() {
  return request({
    url: '/role/allRoles',
    method: 'get'
  })
}

// 获取可以访问某个permission的所有角色信息
export function getRoleNameByPermissionName(permissionName) {
  return request({
    url: '/role/getRoleNameByPermissionName',
    method: 'get',
    params: { permissionName }
  })
}

// 分配角色资源
export function assignRoleMenu(roleId, menuList) {
  return request({
    url: '/role/assignRoleMenu',
    method: 'post',
    params: { roleId },
    data: menuList
  })
}

// 分配角色操作
export function assignRolePermission(roleId, permissionList) {
  return request({
    url: '/role/assignRolePermission',
    method: 'post',
    params: { roleId },
    data: permissionList
  })
}

// 新建角色
export function createRole(data) {
  return request({
    url: '/role/create',
    method: 'post',
    data
  })
}

// 查询角色信息
export function readRole(roleName) {
  return request({
    url: '/role/read',
    method: 'get',
    params: { roleName }
  })
}

// 更新角色信息
export function updateRole(data) {
  return request({
    url: '/role/update',
    method: 'post',
    data
  })
}

// 删除角色信息
export function deleteRole(roleName) {
  return request({
    url: '/role/delete',
    method: 'delete',
    params: { roleName }
  })
}
