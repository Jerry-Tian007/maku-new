import request from "@/utils/request";

// 增
export function addKBHistory(data) {
  return request({
    url: '/gpt/kbqarecord',
    method: 'post',
    data
  })
}

// 查询文件列表
export function listKBHistory(query) {
  return request({
    url: '/gpt/kbqarecord/list',
    method: 'get',
    params: query
  })
}

// 更新文件列表
export function updateKBHistory(data) {
  return request({
    url: '/gpt/kbqarecord',
    method: 'post',
    data
  })
}

// 查询文件列表
export function listLatestKBHistory(query) {
  return request({
    url: '/gpt/kbqarecord/list',
    method: 'get',
    params: query
  })
}
//删除
export function delHistory(ids){
  return request({
    url:'/gpt/kbqarecord/delete/${ids}',
    method:'post',
  })
}
//编辑
export function editHistory(data) {
  return request({
    url: `/gpt/kbqarecord/edit`,
    method: 'post',
    data
  })
}
//导出
export function  exportHistory(ids){
  return request({
    url:'/gpt/kbqarecord/export${ids}',
    method:'post'
  })
}
