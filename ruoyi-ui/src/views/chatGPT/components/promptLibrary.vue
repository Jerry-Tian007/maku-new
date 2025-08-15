<template>
  <el-tabs stretch v-model="libraryTab" align="left" @tab-click="getList()" type="border-card"
           style="border-radius: 10px;">
    <el-tab-pane label="我的" name="individual">
      <div align="center" class="library-search">
        <el-input
          v-model="queryParams.title"
          placeholder="标题"
          prefix-icon="el-icon-search"
          clearable
          @change="handleQuery"
          @clear="handleQuery"
          class="library-search-input"
        />
      </div>
      <el-row v-loading="isLoading" :style="isLoading? 'min-height:60px;':''">
        <el-col
          :span="12"
          style="padding: 10px;">
          <div class="prompt-item">
            <div class="title">{{ '默认' }}</div>
            <div class="tool">
              <el-button
                @click="handleUse({id:0,title:'默认',content:'你是一个喜欢使用中文的助手'})"
                size="mini"
                type="text"
                :style="promptId === 0 ? 'color:#909399' : 'color:#409EFF'"
                :disabled="promptId === 0"
              > {{ promptId === 0 ? '使用中' : '使用' }}
              </el-button>
            </div>
          </div>
        </el-col>
        <el-col
          v-for="item in myList"
          :key="item.id"
          :span="12"
          style="padding: 10px;">
          <div class="prompt-item">
            <div class="title">{{ item.title }}</div>
            <div class="tool">
              <el-button
                @click="handleEdit(item)"
                size="mini"
                type="text"
                style="color: #efbc6e"
              > 编辑
              </el-button><!--
              <el-button
                v-if="false"
                @click="handleDelete(item)"
                type="text"
                style="color: #F56C6C"
              > 删除
              </el-button>-->
              <el-button
                @click="handleUse(item)"
                size="mini"
                type="text"
                :style="promptId === item.id ? 'color:#909399' : 'color:#409EFF'"
                :disabled="promptId === item.id"
              > {{ promptId === item.id ? '使用中' : '使用' }}
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      <el-button
        class="button-hide"
        type="primary"
        size="mini"
        icon="el-icon-plus"
        style="margin: 10px 0 0 10px;"
        @click="handleAdd"
        plain
        :disabled="myList.length >= promptMaxCount"
        :style="(!show ? !queryParams.title ? 'opacity:1;' : 'opacity:0;pointer-events:none;' : 'opacity:0;pointer-events:none;') + 'overflow: hidden;'"
      >创建提示词
        {{ isLoading ? '(    / ' + promptMaxCount + ' )' : ('( ' + myList.length + ' / ' + promptMaxCount + ' )') }}
      </el-button>
      <div class="edit" :style="(show ? 'height:200px;' : 'height:0px;') + 'overflow: hidden;'">
        <el-form ref="form" :model="form" :rules="rules" v-loading="isWaiting" size="small" label-width="60px"
                 label-position="left"
                 @submit.native.prevent
                 style="padding:0 4%;overflow: hidden">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="4"
              resize="none"
              placeholder="请输入内容"
            />
          </el-form-item>
          <div style="width:100%;display: flex;justify-content: space-between;">
            <div>
              <el-button
                :type="form.id ? 'warning' : 'primary'"
                size="mini"
                plain
                @click="handleSubmit()"
              >{{ form.id ? '确认修改' : '确认保存' }}
              </el-button>
              <el-button
                size="mini"
                plain
                @click="handleCancel()"
              >取消
              </el-button>
            </div>
            <div v-if="form.id">
              <el-button
                size="mini"
                type="danger"
                plain
                icon="el-icon-delete"
                @click="handleDelete()"
              >
              </el-button>
            </div>
          </div>
        </el-form>
      </div>
    </el-tab-pane>
    <el-tab-pane label="社区" name="community">
      <div align="right" class="library-search">
        <el-input
          v-model="queryParams.title"
          placeholder="标题"
          prefix-icon="el-icon-search"
          clearable
          @change="handleQuery"
          @clear="handleQuery"
          class="library-search-input"
        />
      </div>
      <el-row v-loading="isLoading" :style="isLoading? 'min-height:60px;':''">
        <el-col
          v-for="item in communityList"
          :key="item.id"
          :span="12"
          style="padding: 10px;">
          <div class="prompt-item">
            <div class="title">{{ item.title }}</div>
            <div class="tool">
              <el-button
                @click="handleSave(item)"
                size="mini"
                type="text"
                :style="item.isSaved ? 'color:#909399' : 'color:#efbc6e'"
                :disabled="item.isSaved"
              > {{ item.isSaved ? '已保存' : '保存' }}
              </el-button>
              <el-button
                @click="handleUse(item)"
                size="mini"
                type="text"
                :style="promptId === item.id ? 'color:#909399' : 'color:#409EFF'"
                :disabled="promptId === item.id"
              > {{ promptId === item.id ? '使用中' : '使用' }}
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
        style="margin-bottom: 20px;"
      />
    </el-tab-pane>
  </el-tabs>
</template>

<script>


import {addPromptlibrary, listPromptlibrary, updatePromptlibrary, delPromptlibrary} from "@/api/chatGPT/promptlibrary";
import {isRelogin} from "../../../utils/request";
import prompt from "../prompt";

export default {
  name: "PromptLibrary",
  props: {
    promptId: {
      type: Number,
      default: 0,
    }
  },
  data() {
    return {
      libraryTab: 'individual',
      show: false,

      myList: [],
      communityList: [],
      total: 0,

      form: {
        title: null,
        content: null,
      },

      isLoading: false,
      isWaiting: false,

      rules: {
        title: [
          {required: true, message: "标题不能为空", trigger: "blur"},
        ],
        content: [
          {required: true, message: "内容不能为空", trigger: "blur"},
        ],
      },

      promptMaxCount: 30,

      queryParams: {
        title: null,
        pageNum: 1,
        pageSize: 10,
      }
    };
  },
  watch: {},
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.isLoading = true;
      listPromptlibrary({...this.queryParams, type: this.libraryTab}).then(res => {
        if (res.code === 200) {
          if (this.libraryTab === 'individual') {
            this.myList = res.rows;
          } else {
            this.communityList = res.rows;
          }
          this.total = res.total;
        } else {
          this.$message.error(res.msg);
          console.log(res.msg)
        }
        this.isLoading = false;
      }).catch(err => {
        this.isLoading = false;
        this.$message.error(err);
        console.log(err);
      })
    },
    scrollLoading() {
      listPromptlibrary({...this.queryParams, type: this.libraryTab}).then(res => {
        if (res.code === 200) {
          if (this.libraryTab === 'individual') {
            this.myList = [...this.myList, res.rows];
          } else {
            this.communityList = [...this.communityList, res.rows];
          }
        } else {
          this.$message.error(res.msg);
          console.log(res.msg)
        }
      }).catch(err => {
        this.$message.error(err);
        console.log(err);
      })
    },
    handleAdd() {
      this.show = false;
      this.form = {
        title: null,
        content: null
      };
      this.show = true;
    },
    handleEdit(item) {
      this.form = {...item};
      this.show = true;
    },
    handleUse(item) {
      this.$confirm('是否确认使用该系统级提示词？\n此操作将替换当前使用的系统级提示词！',
        '替换确认',
        {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
      ).then(() => {
        this.$emit('promptClick', item);
      }).catch(() => {
      });
    },
    handleDelete() {
      if (this.promptId === this.form.id && this.promptId !== 0) {
        this.$confirm('是否确认修改 当前使用的提示词？（修改后会使用默认提示词）',
          '',
          {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
        ).then(() => {
          this.isWaiting = true;
          delPromptlibrary(this.form.id).then(() => {
            this.show = false;
            this.form = {
              title: null,
              content: null
            };
            this.isWaiting = false;
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => {
          });
        }).catch(() => {
        });
      } else {
        this.$confirm('是否确认删除数据项？',
          '',
          {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
        ).then(() => {
          this.isWaiting = true;
          delPromptlibrary(this.form.id).then(() => {
            this.show = false;
            this.form = {
              title: null,
              content: null
            };
            this.isWaiting = false;
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => {
          });
        }).catch(() => {
        });
      }
    },
    handleSave(item) {
      item.isSaved = true;
      console.log(item);
      this.$forceUpdate();
      addPromptlibrary({type: 'individual', title: item.title, content: item.content}).then(res => {
        if (res.code === 200) {
          this.$message.success('保存成功！');
        } else {
          this.$message.error(res.msg);
          console.log(res.msg);
          item.isSaved = false;
          this.$forceUpdate();
        }
      }).catch(err => {
        item.isSaved = false;
        this.$forceUpdate();
        this.$message.error(err);
        console.log(err);
      })
    },
    handleCancel() {
      this.show = false;
      this.form = {
        title: null,
        content: null
      };
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.isWaiting = true;
          this.form.type = 'individual';
          if (this.form.id) {
            if (this.promptId === this.form.id && this.promptId !== 0) {
              this.$confirm('是否确认修改 当前使用的提示词？（修改后会使用默认提示词）',
                '',
                {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
              ).then(() => {
                updatePromptlibrary(this.form).then(res => {
                  if (res.code === 200) {
                    this.$message.success('修改成功！');
                    this.show = false;
                    this.getList();
                  } else {
                    this.$message.error(res.msg);
                    console.log(res.msg)
                  }
                  this.isWaiting = false;
                  this.$emit('promptClick', {id: 0, title: '默认', content: '你是一个喜欢使用中文的助手'});
                }).catch(err => {
                  this.isWaiting = false;
                  this.$message.error(err);
                  console.log(err);
                  this.$emit('promptClick', {id: 0, title: '默认', content: '你是一个喜欢使用中文的助手'});
                })
              }).catch(() => {
                this.isWaiting = false;
              });
            } else {
              updatePromptlibrary(this.form).then(res => {
                if (res.code === 200) {
                  this.$message.success('修改成功！');
                  this.show = false;
                  this.getList();
                } else {
                  this.$message.error(res.msg);
                  console.log(res.msg)
                }
                this.isWaiting = false;
              }).catch(err => {
                this.isWaiting = false;
                this.$message.error(err);
                console.log(err);
              })
            }
          } else {
            addPromptlibrary(this.form).then(res => {
              if (res.code === 200) {
                this.$message.success('创建成功！');
                this.show = false;
                this.getList();
              } else {
                this.$message.error(res.msg);
                console.log(res.msg)
              }
              this.isWaiting = false;
            }).catch(err => {
              this.isWaiting = false;
              this.$message.error(err);
              console.log(err);
            })
          }
        }
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
  },
};
</script>

<style lang="scss" scoped>
.button-hide {
  transition: opacity 0.2s ease-in-out;
}

.edit {
  margin-top: 20px;
  transition: height 0.2s ease-in-out;
}

.prompt-item {
  width: 100%;
  border: 1px solid #E4E7ED;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .title {
    flex: 1;
    padding: 0 10px;
    line-height: 40px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .tool {
    padding: 6px;

    span {
      margin: 0 6px;
    }

  }
}

.library-search-input {
  width: calc(100% - 20px);
  height: 30px;
  margin: 0px 10px 20px 10px;
  transition: all 0.3s ease-in-out;

  ::v-deep .el-input__inner{
    line-height: 30px;
    border-radius: 12px;
  }
}

::v-deep .el-textarea__inner {
  border: 1px solid #DCDFE6 !important;
}
</style>
