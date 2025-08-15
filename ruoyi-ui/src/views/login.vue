<template>
  <div class="login-container">
    <!--    <div class="login-background"></div>-->
    <div class="login">
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <div class="login-logo">
<!--          <div class="IntelligenzImg">
          </div>-->
          <img :src="logoIcon" alt="" class="IntelligenzImg" style="background: none;">
        </div>
        <div class='login-Da-linker'>
          NY Zodiac AI Studio
        </div>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            :placeholder="isAd? '邮箱' : '账号'"
          ><!--
            <template slot="suffix">
              <span v-if="loginForm.username === '' && loginForm.username !== undefined"
                    class="input-error">{{ loginRules.username[0].message }}</span>
            </template>-->
            <svg-icon
              slot="prefix"
              icon-class="user"
              class="el-input__icon input-icon"
              style="color:#1F295E;"
            />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
          >
<!--            <template slot="suffix">
              <span v-if="loginForm.password === '' && loginForm.password !== undefined"
                    class="input-error">{{ loginRules.password[0].message }}</span>
            </template>-->
            <svg-icon
              slot="prefix"
              icon-class="password"
              class="el-input__icon input-icon"
              style="color:#55648A;"
            />
          </el-input>
        </el-form-item>
        <el-form-item
          prop="code"
          v-show="!isAd"
          v-if="captchaEnabled"
        >
          <el-input
            v-model="loginForm.code"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%;float: left;"
            @keyup.enter.native="handleLogin"
          >
<!--            <template slot="suffix">
              <span v-if="loginForm.code === '' && loginForm.code !== undefined"
                    class="input-error">{{ loginRules.code[0].message }}</span>
            </template>-->
            <svg-icon
              slot="prefix"
              icon-class="validCode"
              class="el-input__icon input-icon"
              style="color: #1F295E;"
            />
          </el-input>
          <div class="login-code">
            <img
              :src="codeUrl"
              @click="getCode"
              class="login-code-img"
            />
          </div>
        </el-form-item>
        <el-form-item style="width:100%;height:20px;">
          <el-checkbox
            v-model="loginForm.rememberMe"
            style="float: left;line-height:21px;"
          >记住密码
          </el-checkbox>
          <el-button
            v-if="false"
            type="text"
            @click="getLoginType()"
            :disabled="loading"
            style="float: right;line-height:0px;height:20px;"
          >{{ isAd ? '<< 普通登录' : '域账号登录 >>' }}
          </el-button>
        </el-form-item>
        <el-form-item style="width:100%;" v-show="isAd">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;background: linear-gradient( 270deg, #2B9ED7 0%, #2F53CE 100%);border-radius: 4px;height: 50px;margin-top: 47px;"
            @click.native.prevent="handleLoginByAD"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
          <div
            style="float: right;"
            v-if="register"
          >

          </div>
        </el-form-item>
        <el-form-item style="width:100%;" v-show="!isAd">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;background: linear-gradient( 270deg, #2B9ED7 0%, #2F53CE 100%);border-radius: 4px;height: 50px;"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
          <div
            style="float: right;"
            v-if="register"
          >

          </div>
        </el-form-item>
        <div class="tableTtile" v-if="false">
          <img
            src="../assets/images/tableTtile.png"
            alt=""
          >
        </div>
      </el-form>

      <!--  底部  -->
      <div class="el-login-footer">
        <span>{{copyRight}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {getCodeImg} from "@/api/login";
import Cookies from "js-cookie";
import {encrypt, decrypt} from '@/utils/jsencrypt'
import logoIcon from '@/assets/logo/logoIcon1.png'

export default {
  name: "Login",
  data() {
    return {
      logoIcon,
      codeUrl: "",
      loginForm: {
        username: "",//默认账号admin
        password: "",//默认密码admin123
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          {required: true, trigger: "blur", message: "请输入您的账号"}
        ],
        password: [
          {required: true, trigger: "blur", message: "请输入您的密码"}
        ],
        code: [{required: true, trigger: "change", message: "请输入验证码"}]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined,
      isAd: false,

      copyRight: 'Copyright © 2018-2022 nysoftland.com All Rights Reserved.'
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
    if (localStorage.getItem('loginAd')) {
      this.isAd = localStorage.getItem('loginAd') == 'true' ? true : false;
    }
  },
  methods: {
    getLoginType() {
      this.isAd = !this.isAd;
      localStorage.setItem('loginAd', this.isAd);
      this.loginRules.username[0].message = "请输入您的" + (this.isAd ? '邮箱' : '账号');
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLoginByAD() {
      this.loading = true;
      this.$store.dispatch("LoginByAD", this.loginForm).then(() => {
        this.$router.push({path: this.redirect || "/"}).catch(() => {
        });
      }).catch(() => {
        this.loading = false;
        if (this.captchaEnabled) {
          this.getCode();
        }
      });
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, {expires: 30});
            Cookies.set("password", encrypt(this.loginForm.password), {expires: 30});
            Cookies.set('rememberMe', this.loginForm.rememberMe, {expires: 30});
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url(../assets/images/WechatIMG487.png);
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;

  .login-background {
    flex-grow: 1;
  }

  .login {
    .login-logo {
      display: flex;
      justify-content: center;
      align-items: center;

      .IntelligenzImg {
        margin-bottom: 9px;
        width: 80px;
        height: 80px;
        background: linear-gradient(270deg, #CBF6E7 0%, #83B8E9 100%);
        border-radius: 18px;
      }
    }

    .login-Da-linker {
      margin-bottom: 30px;
      font-family: BMJUAOTF;
      font-size: 24px;
      color: #FFFFFF;
      line-height: 25px;
      text-align: center;
      font-style: normal;
    }

    .tableTtile {
      margin-top: 20px;
      float: right;

      img {
        width: 150px;
      }
    }
  }
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: auto;
  background-size: cover;
  background: rgba(17, 16, 16, 0.31);
  border-radius: 4px;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  width: 450px;
  padding: 25px 50px 5px 50px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}

.input-error {
  color: red;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

::v-deep .el-form-item__error {
  padding-top: 0;
  position: absolute;
  top: 0;
  right: 10px;
  bottom: 0;
  left: auto;
  height: fit-content;
  width: fit-content;
  margin: auto;
}

::v-deep .el-checkbox__label {
  width: 57px;
  height: 20px;
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #FFFFFF !important;
  line-height: 20px;
  text-align: center;
  font-style: normal;
}

::v-deep .el-button--text {
  font-family: PingFangSC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #FFFFFF;
  line-height: 20px;
  text-align: center;
  font-style: normal;
}

::v-deep .el-button--primary {
  font-weight: 550;
  font-size: 14px;
  color: #FFFFFF;
  line-height: 25px;
  text-align: center;
  font-style: normal;
}


::v-deep .el-checkbox__inner {
  //border: 1px solid transparent;//外层边框
  background-color: transparent;//!!未选中内部背景为透明
}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  //border-color: #FFFFFF;
  background-color: #FFFFFF;//选中时内部背景为白色
  border: 2px solid transparent;
}

::v-deep .el-checkbox__input.is-focus .el-checkbox__inner, .el-checkbox__inner:hover {
  border-color: #FFFFFF;
}

::v-deep .el-checkbox__inner::after {//内层边框
  border: 2px solid #1B3B71;
}
</style>
