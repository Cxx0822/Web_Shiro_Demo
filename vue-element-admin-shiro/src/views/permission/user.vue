<template>
  <div class="app-container">
    <!--新建用户-->
    <el-button v-if="checkPermission(createUserRoles)" type="primary" @click="handleCreateUser">新建用户</el-button>

    <el-table :data="userInfoList" style="width:1251px;margin-top:10px;" border>
      <el-table-column align="center" label="序号" width="50">
        <template slot-scope="scope">
          {{ scope.$index+1 }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="用户名" width="150">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="姓名(昵称)" width="150">
        <template slot-scope="scope">
          {{ scope.row.nickname }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="用户描述" width="300">
        <template slot-scope="scope">
          {{ scope.row.introduction }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="头像" width="100">
        <template slot-scope="scope">
          {{ scope.row.avatar }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="500">
        <template slot-scope="scope">
          <el-button v-if="checkPermission(updateUserRoles)" type="primary" @click="handleUpdateUser(scope)">更新用户信息</el-button>
          <el-button v-if="checkPermission(assignUserRoles)" type="primary" @click="handleAssignUser(scope)">分配用户角色</el-button>
          <el-button v-if="checkPermission(deleteUserRoles)" type="danger" @click="handleDeleteUser(scope)">删除用户</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/更新用户对话框 -->
    <el-dialog :visible.sync="dialogCreateOrUpdateUserVisible" :title="dialogCreateOrUpdateUserTitle">
      <el-form
        ref="createOrUpdateUserForm"
        :model="newUser"
        :rules="createOrUpdateUserRules"
        label-width="80px"
        label-position="left"
      >
        <el-form-item prop="username" label="用户名">
          <el-input v-model="newUser.username" :disabled="isUsernameDisable" placeholder="用户名" />
        </el-form-item>

        <el-form-item prop="password" label="密码">
          <el-input v-model="newUser.password" type="password" placeholder="密码" />
        </el-form-item>

        <el-form-item prop="confirmPassword" label="确认密码">
          <el-input v-model="newUser.confirmPassword" type="password" placeholder="确认密码" />
        </el-form-item>

        <el-form-item label="昵称">
          <el-input v-model="newUser.nickname" placeholder="昵称" />
        </el-form-item>

        <el-form-item label="用户描述">
          <el-input
            v-model="newUser.introduction"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="用户描述"
          />
        </el-form-item>

        <el-form-item label="头像">
          <el-input v-model="newUser.avatar" placeholder="头像" />
        </el-form-item>
      </el-form>

      <div style="text-align:right;">
        <el-button type="danger" @click="dialogCreateOrUpdateUserVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmCreateOrUpdateUser">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogAssignUserVisible" :title="dialogAssignUserTitle">
      <h3 style="margin-bottom:30px">角色列表</h3>
      <!-- 当前用户的角色信息 只能选择一个角色 -->
      <el-checkbox-group v-model="currentUserRoleList" :max="1">
        <el-checkbox v-for="(role, index) in roleList" :key="index" :label="role.roleName" />
      </el-checkbox-group>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogAssignUserVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmAssignUser">提交</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getAllUserRoleInfo, assignUserRoles, createUser, deleteUser, readUser, updateUser } from '@/api/user'
import { deepClone } from '@/utils'
import { getAllRoles, getRoleNameByPermissionName } from '@/api/role'
import checkPermission from '@/utils/permission' // 权限判断函数

const defaultUser = {
  username: '',
  nickname: '',
  introduction: '',
  avatar: ''
}

export default {
  data() {
    const validateUsername = (rule, value, callback) => {
      // 自定义用户名认证
      if (value.length < 1) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('请输入不少于6位密码'))
      } else {
        callback()
      }
    }
    return {
      user: Object.assign({}, defaultUser),
      userInfoList: [],
      dialogAssignUserVisible: false,

      // 新建/更新 用户控制
      dialogCreateOrUpdateUserTitle: '新建用户',
      dialogCreateOrUpdateUserVisible: false,
      isUsernameDisable: false,

      dialogAssignUserTitle: '',

      newUser: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        introduction: '',
        avatar: ''
      },
      // 数据库密码
      dbPassword: '',
      createOrUpdateUserRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },

      roleList: [],
      // 当前用户的角色信息
      currentUserRoleList: [],

      // 具体 创建用户 功能的角色
      createUserRoles: [],
      // 具有 更新用户 功能的角色
      updateUserRoles: [],
      // 具有 分配用户角色 功能的角色
      assignUserRoles: [],
      // 具有 删除用户 功能的角色
      deleteUserRoles: []
    }
  },
  computed: {
    routesData() {
      return this.routes
    }
  },
  created() {
    // 后端获取数据
    this.getUserList()
    this.getRoleList()
    this.getOperateRoles()
  },
  methods: {
    // 引入checkPermission()
    checkPermission,
    // 获取所有用户信息
    async getUserList() {
      const res = await getAllUserRoleInfo()
      this.userInfoList = res.data.data
    },
    // 获取所有角色信息
    async getRoleList() {
      const res = await getAllRoles()
      this.roleList = res.data.data
    },
    // 获取 能够操纵 各个操作权限的 角色信息
    async getOperateRoles() {
      // 获取能够操作 创建用户 功能的所有角色名称
      let res = await getRoleNameByPermissionName('createUser')
      this.createUserRoles = res.data.data

      // 获取能够操作 更新用户 功能的所有角色名称
      res = await getRoleNameByPermissionName('updateUser')
      this.updateUserRoles = res.data.data

      // 获取能够操作 分配用户角色 功能的所有角色名称
      res = await getRoleNameByPermissionName('assignUserRole')
      this.assignUserRoles = res.data.data

      // 获取能够操作 删除用户 功能的所有角色名称
      res = await getRoleNameByPermissionName('deleteUser')
      this.deleteUserRoles = res.data.data
    },
    // 分配用户的角色权限 按钮
    handleAssignUser(scope) {
      // 复制一份user
      this.user = deepClone(scope.row)
      this.dialogAssignUserVisible = true
      this.dialogAssignUserTitle = '给 ' + scope.row.username + ' 分配角色'
      this.$nextTick(() => {
        this.currentUserRoleList.splice(0, this.currentUserRoleList.length)
        const roles = scope.row.roleList
        roles.forEach(role => {
          // 将该用户的角色信息放到选项框中
          this.currentUserRoleList.push(role.roleName)
        })
      })
    },
    // 确定 分配用户角色 按钮
    async confirmAssignUser() {
      // 目前业务为：一个用户有且仅有一个角色
      if (this.currentUserRoleList.length === 0) {
        this.$message.error('请分配用户角色')
      } else {
        // 分配用户角色 参数：[用户名,角色]
        await assignUserRoles(this.user.username, this.currentUserRoleList[0])
        this.$message.success('分配角色成功')
        this.dialogAssignUserVisible = false
      }
      // 重新获取用户信息
      this.getUserList()
    },
    // 新建用户按钮
    handleCreateUser() {
      this.dialogCreateOrUpdateUserVisible = true
      this.dialogCreateOrUpdateUserTitle = '新建用户'
    },
    // 确定新建/更新用户按钮
    async confirmCreateOrUpdateUser() {
      // 表单验证
      this.$refs.createOrUpdateUserForm.validate(async valid => {
        if (valid) {
          if (this.newUser.password !== this.newUser.confirmPassword) {
            this.$message.error('密码不一致')
          } else {
            // 需要复制一份，后面有删除操作
            const newUpdateUser = deepClone(this.newUser)
            // 新建/更新的用户角色信息 注意删除用户的确认密码
            delete newUpdateUser.confirmPassword
            if (this.dialogCreateOrUpdateUserTitle === '更新用户') {
              // 没有更新密码
              if (this.dbPassword === newUpdateUser.password) {
                delete newUpdateUser.password
              }
              await updateUser(newUpdateUser)
              this.$message.success('更新用户成功')
            } else {
              await createUser(newUpdateUser)
              this.$message.success('新建用户成功')
            }
            for (const key in this.newUser) {
              this.newUser[key] = ''
            }
            this.dialogCreateOrUpdateUserVisible = false
            // 重新获取用户信息
            this.getUserList()
          }
        } else {
          return false
        }
      })
    },
    // 更新用户按钮
    async handleUpdateUser({ row }) {
      this.dialogCreateOrUpdateUserVisible = true
      this.dialogCreateOrUpdateUserTitle = '更新用户'
      this.isUsernameDisable = true

      // 查找当前用户信息 并显示到对话框中
      this.newUser.username = row.username
      const res = await readUser(row.username)
      const userInfo = res.data.data[0]

      // 赋值到对话框中
      for (const key in userInfo) {
        this.newUser[key] = userInfo[key]
      }
      // MD5只能正向加密 不能逆向解密
      // 数据库存的是密文密码 无法获取原始输入密码
      // 这里采用 保存数据库密码，如果没有更新密码，则不提交密码信息 的解决方案
      this.dbPassword = this.newUser['password']
      this.newUser['confirmPassword'] = this.newUser['password']
    },
    // 删除用户按钮
    handleDeleteUser({ $index, row }) {
      this.$confirm('确定要删除该用户吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await deleteUser(row.username)
          // 删除列表中的用户数据
          this.userInfoList.splice($index, 1)
          this.$message.success('删除用户成功!')
        })
        .catch(err => { console.error(err) })
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
