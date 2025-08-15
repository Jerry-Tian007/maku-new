import request from '@/utils/request'

// 查询promptLibrary列表
export function listPromptlibrary(query) {
  return request({
    url: '/gpt/promptlibrary/list',
    method: 'get',
    params: query
  })
}

// 查询promptLibrary详细
export function getPromptlibrary(id) {
  return request({
    url: '/gpt/promptlibrary/' + id,
    method: 'get'
  })
}

// 新增promptLibrary
export function addPromptlibrary(data) {
  return request({
    url: '/gpt/promptlibrary',
    method: 'post',
    data: data
  })
}

// 修改promptLibrary
export function updatePromptlibrary(data) {
  return request({
    url: '/gpt/promptlibrary',
    method: 'put',
    data: data
  })
}

// 删除promptLibrary
export function delPromptlibrary(id) {
  return request({
    url: '/gpt/promptlibrary/' + id,
    method: 'delete'
  })
}

export function toneStyleDetail(query) {
  return request({
    url: '/gpt/tonestyle/detail',
    headers: {
      repeatSubmit: false,
    },
    method: 'get',
    params: query
  })
}
