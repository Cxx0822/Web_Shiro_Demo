<template>
  <div class="app-container">
    <!--新建角色-->
    <el-button v-if="checkPermission(createRoleRoles)" type="primary" @click="handleCreateRole">新建角色</el-button>

    <!--显示角色信息列表-->
    <el-table :data="rolesList" style="width:951px;margin-top:30px;" border>
      <el-table-column align="center" label="序号" width="50">
        <template slot-scope="scope">
          {{ scope.$index+1 }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色名称" width="150">
        <template slot-scope="scope">
          {{ scope.row.roleName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色描述" width="200">
        <template slot-scope="scope">
          {{ scope.row.roleDescription }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="550">
        <template slot-scope="scope">
          <el-button v-if="checkPermission(updateRoleRoles)" type="primary" @click="handleUpdateRole(scope)">更新角色信息</el-button>
          <el-button v-if="checkPermission(assignRoleMenuRoles)" type="primary" @click="handleAssignRoleMenu(scope)">分配角色资源</el-button>
          <el-button v-if="checkPermission(assignRolePermissionRoles)" type="primary" @click="handleAssignRolePermission(scope)">分配角色操作</el-button>
          <el-button v-if="checkPermission(deleteRoleRoles)" type="danger" @click="handleDeleteRole(scope)">删除角色</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新建/更新角色对话框 -->
    <el-dialog :visible.sync="dialogCreateOrUpdateRoleVisible" :title="dialogCreateOrUpdateRoleTitle">
      <el-form
        ref="createOrUpdateRoleForm"
        :model="role"
        :rules="createOrUpdateRoleRules"
        label-width="80px"
        label-position="left"
      >
        <el-form-item prop="roleName" label="角色名称">
          <el-input v-model="role.roleName" placeholder="角色名称" />
        </el-form-item>

        <el-form-item label="角色描述">
          <el-input
            v-model="role.roleDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="角色描述"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogCreateOrUpdateRoleVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmCreateOrUpdateRole">提交</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色资源 对话框 -->
    <el-dialog :visible.sync="dialogAssignRoleMenuVisible" title="分配角色资源">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" disabled placeholder="角色名称" />
        </el-form-item>

        <el-form-item label="角色描述">
          <el-input
            v-model="role.roleDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            disabled
            placeholder="角色描述"
          />
        </el-form-item>

        <el-form-item label="路由树">
          <el-tree
            ref="menuTree"
            :check-strictly="checkStrictly"
            :data="routesData"
            :props="defaultProps"
            show-checkbox
            default-expand-all
            node-key="id"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogAssignRoleMenuVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmAssignRoleMenu">提交</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色操作 对话框 -->
    <el-dialog :visible.sync="dialogAssignRolePermissionVisible" :title="'分配角色操作'">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" placeholder="角色名称" disabled />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input
            v-model="role.roleDescription"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="角色描述"
            disabled
          />
        </el-form-item>
        <el-form-item label="角色权限">
          <el-tree
            ref="permissionTree"
            :check-strictly="checkStrictly"
            :data="resourceList"
            :props="permTreeDefaultProps"
            show-checkbox
            default-expand-all
            node-key="id"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>

      <div style="text-align:right;">
        <el-button type="danger" @click="dialogAssignRolePermissionVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmAssignRolePermission">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import path from 'path'
import qs from 'qs'
import { deepClone } from '@/utils'
import { getAllRoles, getRoleNameByPermissionName, createRole, readRole, deleteRole, updateRole, assignRoleMenu, assignRolePermission } from '@/api/role'
import { getRoutes } from '@/api/user'
import { getResources } from '@/api/resource'
import checkPermission from '@/utils/permission' // 权限判断函数

// 定义一个角色对象（初始化）
const defaultRole = {
  id: '',
  roleName: '',
  roleDescription: '',
  routes: [],
  permissionList: []
}

export default {
  data() {
    const validateRoleName = (rule, value, callback) => {
      // 自定义角色名认证
      if (value.length < 1) {
        callback(new Error('请输入角色名'))
      } else {
        callback()
      }
    }
    return {
      // 角色属性-绑定到角色表单上
      role: Object.assign({}, defaultRole),
      // 存放当前角色拥有的路由
      routes: [],
      // 角色表个显示的角色集合
      rolesList: [],

      // 新建/更新 角色控制
      dialogCreateOrUpdateRoleVisible: false,
      dialogCreateOrUpdateRoleTitle: '新建角色',

      // 分配角色路由
      dialogAssignRoleMenuVisible: false,

      createOrUpdateRoleRules: {
        roleName: [{ required: true, trigger: 'blur', validator: validateRoleName }]
      },

      dialogAssignRolePermissionVisible: false,

      // 父子节点 checkbox的选择是否关联
      checkStrictly: false,
      // 角色树
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      // 权限树
      permTreeDefaultProps: {
        children: 'permissionList',
        label: 'title'
      },
      // 创建角色
      createRoleRoles: [],
      // 更新角色
      updateRoleRoles: [],
      // 分配角色菜单
      assignRoleMenuRoles: [],
      // 分配角色操作
      assignRolePermissionRoles: [],
      // 删除角色
      deleteRoleRoles: [],
      resourceList: []
    }
  },
  computed: {
    routesData() {
      return this.routes
    }
  },
  created() {
    // 获取初始信息
    this.getRoutes()
    this.getRoles()
    this.getOperateRoles()
    this.getResourceList()
  },
  methods: {
    // 注册import的checkPermission方法
    checkPermission,
    // 获取路由信息
    async getRoutes() {
      const res = await getRoutes()
      // 保存路由数据
      this.serviceRoutes = res.data.data
      // 将路由对象，整理成{path， title， id}
      this.routes = this.generateRoutes(this.serviceRoutes)
    },

    // 获取角色信息
    async getRoles() {
      const res = await getAllRoles()
      this.rolesList = res.data.data
    },

    // 获取操作信息
    async getOperateRoles() {
      // this.createRoleRoles = ['admin']
      // 获取能够操作 创建角色 功能的所有角色名称
      let res = await getRoleNameByPermissionName('createRole')
      this.createRoleRoles = res.data.data

      // 获取能够操作 更新角色 功能的所有角色信息
      res = await getRoleNameByPermissionName('updateRole')
      this.updateRoleRoles = res.data.data

      // 获取能够操作 分配角色资源 功能的所有角色名称
      res = await getRoleNameByPermissionName('assignRoleMenu')
      this.assignRoleMenuRoles = res.data.data

      // 获取能够操作 分配角色操作 功能的所有角色名称
      res = await getRoleNameByPermissionName('assignRolePermission')
      this.assignRolePermissionRoles = res.data.data

      // 获取能够操作 删除角色 功能的所有角色名称
      res = await getRoleNameByPermissionName('deleteRole')
      this.deleteRoleRoles = res.data.data
    },

    // 获取资源信息
    async getResourceList() {
      var res = await getResources()
      this.resourceList = res.data.data
      // 给resource和permssion添加title属性，以便于显示树
      this.resourceList.forEach(res => {
        res.id = 10000 + res.id
        res.title = res.resourceDescription
        res.permissionList.forEach(perm => {
          perm.title = perm.permissionDescription
          perm.id = 0 + perm.id
        })
      })
    },

    async getUpdateRoles() { // updateRolePermission
      const res = await getRoleNameByPermissionName('updateRolePermission') // 获取能够操作“创建角色”功能的所有角色id
      this.updateRoleRoles = res.data
    },

    // Reshape the routes structure so that it looks the same as the sidebar
    generateRoutes(routes, basePath = '/') {
      const res = []

      for (const route of routes) {
        const data = {
          path: path.resolve(basePath, route.path),
          title: route.meta && route.meta.title,
          id: route.id
        }

        // recursive child routes
        if (route.children) {
          data.children = this.generateRoutes(route.children, data.path)
        }
        res.push(data)
      }
      return res
    },

    generateArr(routes) {
      let data = []
      routes.forEach(route => {
        // data.push(route)
        if (route.children) {
          const temp = this.generateArr(route.children)
          if (temp.length > 0) {
            data = [...data, ...temp]
          }
        } else {
          data.push(route.id)
        }
      })
      return data
    },

    // 新建 角色信息
    handleCreateRole() {
      this.dialogCreateOrUpdateRoleTitle = '新建角色'
      this.dialogCreateOrUpdateRoleVisible = true
    },

    // 更新 角色信息
    async handleUpdateRole({ row }) {
      this.dialogCreateOrUpdateRoleTitle = '更新角色'
      this.dialogCreateOrUpdateRoleVisible = true

      // 查找当前角色信息 并显示到对话框中
      this.role.roleName = row.roleName
      const res = await readRole(row.roleName)
      const roleInfo = res.data.data[0]

      // 赋值到对话框中
      for (const key in roleInfo) {
        this.role[key] = roleInfo[key]
      }
    },
    // 确定新建/更新角色按钮
    async confirmCreateOrUpdateRole() {
      // 表单验证
      this.$refs.createOrUpdateRoleForm.validate(async valid => {
        if (valid) {
          // 复制一份
          const newUpdateRole = deepClone(this.role)
          delete newUpdateRole.routes
          delete newUpdateRole.permissionList
          if (this.dialogCreateOrUpdateRoleTitle === '更新角色') {
            await updateRole(newUpdateRole)
            this.$message.success('更新角色成功')
          } else {
            await createRole(newUpdateRole)
            this.$message.success('创建角色成功')
          }

          // 清空角色信息
          for (const key in this.role) {
            this.role[key] = ''
          }
          // 重新获取最新的角色列表
          this.getRoles()
          this.dialogCreateOrUpdateRoleVisible = false
        } else {
          return false
        }
      })
    },

    // 分配角色资源
    handleAssignRoleMenu(scope) {
      this.dialogAssignRoleMenuVisible = true
      this.checkStrictly = true
      this.role = deepClone(scope.row)
      this.$nextTick(() => {
        // const routes = this.generateRoutes(this.role.routes)
        // this.$refs.menuTree.setCheckedNodes(this.generateArr(routes))
        // set checked state of a node not affects its father and child nodes
        const selectedKeys = this.generateArr(this.role.routes)
        this.$refs.menuTree.setCheckedKeys(selectedKeys)
        this.checkStrictly = false
      })
    },

    // 分配角色操作
    handleAssignRolePermission(scope) {
      const rowRole = deepClone(scope.row)
      this.role = rowRole
      this.dialogAssignRolePermissionVisible = true
      this.checkStrictly = false
      this.$nextTick(() => {
        const selectedKeys = []
        this.role.permissionList.forEach(perm => {
          selectedKeys.push(perm.id)
        })
        this.$refs.permissionTree.setCheckedKeys(selectedKeys)
      })
    },

    // 删除角色
    handleDeleteRole({ $index, row }) {
      this.$confirm('确定要删除该角色吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await deleteRole(row.roleName)
          this.rolesList.splice($index, 1)
          this.$message.success('删除成功!')
        })
        .catch(err => { console.error(err) })
    },

    // 确定 分配角色资源 按钮
    async confirmAssignRoleMenu() {
      // 获取选中的树id
      const selectedKeys = this.$refs.menuTree.getCheckedKeys()
      this.role.permissionList = []
      if (selectedKeys.length > 0) {
        selectedKeys.forEach(key => {
          if (key < 10000) {
            this.role.permissionList.push(parseInt(key))
          }
        })
      }
      // 需要利用qs重新序列号字符串，否则不能正确传入list数据
      const menuList = qs.stringify(
        {
          menuList: this.role.permissionList
        },
        {
          indices: false
        })
      await assignRoleMenu(this.role.id, menuList)
      this.$message.success('分配角色资源成功')
      this.dialogAssignRoleMenuVisible = false
      this.getRoles()
    },
    // 确定 分配角色操作 按钮
    async confirmAssignRolePermission() {
      // 获取选中的树id
      const selectedKeys = this.$refs.permissionTree.getCheckedKeys()
      this.role.permissionList = []
      if (selectedKeys.length > 0) {
        selectedKeys.forEach(key => {
          if (key < 10000) {
            this.role.permissionList.push(parseInt(key))
          }
        })
      }
      // 需要利用qs重新序列号字符串，否则不能正确传入list数据
      const permissionList = qs.stringify(
        {
          permissionList: this.role.permissionList
        },
        {
          indices: false
        })
      await assignRolePermission(this.role.id, permissionList)
      this.$message.success('分配角色操作成功')
      this.dialogAssignRolePermissionVisible = false
      this.getRoles()
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
