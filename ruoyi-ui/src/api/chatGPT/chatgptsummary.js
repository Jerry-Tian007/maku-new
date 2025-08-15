import request from '@/utils/request'

// 查询gpt总结列表
export function listChatgptsummary(query) {
  return request({
    url: '/gpt/chatgptsummary/list',
    method: 'get',
    params: query
  })
}

// 查询gpt总结列表
export function identityChatgptsummary(identityId) {
  return request({
    url: '/gpt/chatgptsummary/list?identityId='+identityId,
    method: 'get',
  })
}

// 查询gpt总结列表
export function identityChatgptsummaryLatest(identityId) {
  return request({
    url: '/gpt/chatgptsummary/list24?identityId='+identityId,
    method: 'get',
  })
}

// 查询gpt总结详细
export function getChatgptsummary(id) {
  return request({
    url: '/gpt/chatgptsummary/' + id,
    method: 'get'
  })
}

// 新增gpt总结
export function addChatgptsummary(data) {
  return request({
    url: '/gpt/chatgptsummary',
    method: 'post',
    data: data
  })
}

// 修改gpt总结
export function updateChatgptsummary(data) {
  return request({
    url: '/gpt/chatgptsummary',
    method: 'put',
    data: data
  })
}

// 删除gpt总结
export function delChatgptsummary(id) {
  return request({
    url: '/gpt/chatgptsummary/' + id,
    method: 'delete'
  })
}
