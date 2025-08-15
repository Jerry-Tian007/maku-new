<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item :label="$t('systemManager.oldPassword')" prop="oldPassword">
      <el-input v-model="user.oldPassword" :placeholder="$t('systemManager.oldPasswordPlaceholder')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('systemManager.newPasswords')" prop="newPassword">
      <el-input v-model="user.newPassword" :placeholder="$t('systemManager.newPasswordPlaceholder')" type="password" show-password/>
    </el-form-item>
    <el-form-item :label="$t('systemManager.okPassword')" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" :placeholder="$t('systemManager.okPasswordPlaceholder')" type="password" show-password/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('save') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('close') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from "@/api/system/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error(this.$t('systemManager.twoPassword')));
      } else {
        callback();
      }
    };
    return {
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: this.$t('systemManager.oldPasswordNoNull'), trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: this.$t('systemManager.newPasswordNoNull'), trigger: "blur" },
          { min: 6, max: 20, message: this.$t('systemManager.lengthFromCharacters'), trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: this.$t('systemManager.okPasswordPlaceholderNoNull'), trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(response => {
            this.$modal.msgSuccess(this.$t('updateSuccess'));
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
