import { login, logout, getInfo, loginByAD } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { loginOA, loginWeChat } from "../../api/login";
import { listBots } from "@/api/chat";
import { getModelTemplateSelect } from "@/api/modelManager/modelManager";

const user = {
  state: {
    token: getToken(),
    admin: false,
    nickName: '',
    name: '',
    avatar: '',
    userId: '',
    deptId: '',
    deptName: '',
    roles: [],
    permissions: [],
    roleId: 0,
    userIdentity: {},
    botList: [],
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ADMIN: (state, admin) => {
      state.admin = admin
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_USERID: (state, id) => {
      state.userId = id
    },
    SET_DEPTID: (state, id) => {
      state.deptId = id
    },
    SET_DEPTNAME: (state, deptName) => {
      state.deptName = deptName
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_IDENTITY: (state, userIdentity) => {
      state.userIdentity = userIdentity
    },
    SET_BOTLIST: (state, botList) => {
      state.botList = botList
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },
    LoginOA({ commit }, userIdentity) {
      commit('SET_IDENTITY', userIdentity)
      return new Promise((resolve, reject) => {
        loginOA(userIdentity).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    LoginWeChat({ commit }, query) {
      return new Promise((resolve, reject) => {
        loginWeChat({ code: query.code }).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          let userIdentity = {
            "businessId": query.businessId,
            "identityId": res.identityId,
            "identityName": res.identityName
          }
          localStorage.setItem("query", JSON.stringify(userIdentity));
          commit('SET_IDENTITY', userIdentity);
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.user
          const avatar = (user.avatar == "" || user.avatar == null) ? require("@/assets/images/profile.png") : process.env.VUE_APP_BASE_API + user.avatar;
          commit('SET_USERID', user.userId)
          commit('SET_DEPTID', user.deptId)
          commit('SET_ADMIN', user.admin)
          commit('SET_DEPTNAME', user.dept.deptName)
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NICKNAME', user.nickName)
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetBotList({ commit, state }) {
      return new Promise((resolve, reject) => {
        getModelTemplateSelect().then(res => {
          if (res.code === 200) {
            res.data.forEach(item => {
              item.disable = false;
            });
            commit('SET_BOTLIST', res.data)
            resolve(res)
          } else {
            reject(res.msg)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },
    setBotList({ commit }, status) {
      commit('SET_BOTLIST', status)
    }
  }
}

export default user
