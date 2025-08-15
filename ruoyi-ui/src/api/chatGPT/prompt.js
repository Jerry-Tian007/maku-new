import request from '@/utils/request'

// 查询gpt提示语列表
export function listPrompt(query) {
  return request({
    url: '/gpt/prompt/list',
    method: 'get',
    params: query
  })
}

export function getPromptAllList(params) {
  return request({
    url: '/gpt/prompt/allList',
    method: 'get',
    params: params
  })
}

// 查询gpt提示语详细
export function getPrompt(id) {
  return request({
    url: '/gpt/prompt/' + id,
    method: 'get'
  })
}

// 新增gpt提示语
export function addPrompt(data) {
  return request({
    url: '/gpt/prompt',
    method: 'post',
    data: data
  })
}

// 修改gpt提示语
export function updatePrompt(data) {
  return request({
    url: '/gpt/prompt',
    method: 'put',
    data: data
  })
}

// 删除gpt提示语
export function delPrompt(id) {
  return request({
    url: '/gpt/prompt/' + id,
    method: 'delete'
  })
}
