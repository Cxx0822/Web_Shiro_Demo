import { getRoutes } from '@/api/user'
import { constantRoutes, componentMap } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  // 判断meta和meta里面的roles是否有值
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

// 替换route对象中的component
function replaceComponent(comp) {
  if (comp.component && typeof (comp.component) === 'string') {
    comp.component = componentMap[comp.component]
  }

  if (comp.children && comp.children.length > 0) {
    for (let i = 0; i < comp.children.length; i++) {
      comp.children[i] = replaceComponent(comp.children[i])
    }
  }
  return comp
}

const actions = {
  // 根据用户生成动态路由
  // 修改为异步函数
  async generateRoutes({ commit }, roles) {
    // return new Promise(resolve => {
    //   let accessedRoutes
    //   if (roles.includes('admin')) {
    //     accessedRoutes = asyncRoutes || []
    //   } else {
    //     accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    //   }
    //   commit('SET_ROUTES', accessedRoutes)
    //   resolve(accessedRoutes)
    // })

    // 1.从后端数据库中获取所有的路由信息
    const res = await getRoutes()
    const dbAsyncRoutes = res.data.data

    // 2.过滤路由
    const myAsyncRoutes = dbAsyncRoutes.filter(curr => {
      if (curr.children == null || curr.children.length === 0) {
        delete curr.children
      }
      return replaceComponent(curr)
    })

    // 3.根据角色动态生成路由
    let accessedRoutes
    // 判断当前的角色列表中，是否有包含admin
    // 传入的roles信息为role_name

    if (roles.includes('admin')) {
      // 所有路由都可以被访问，将ansyncRoutes改成从数据库中获取
      accessedRoutes = myAsyncRoutes || []
    } else {
      // 根据角色，过滤掉不能访问的路由表
      accessedRoutes = filterAsyncRoutes(myAsyncRoutes, roles)
    }
    commit('SET_ROUTES', accessedRoutes)
    return accessedRoutes
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
