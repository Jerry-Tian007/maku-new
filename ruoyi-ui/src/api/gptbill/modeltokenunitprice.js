import request from '@/utils/request'

// 查询模型单位token价格列表
export function listModeltokenunitprice(query) {
  return request({
    url: '/gptbill/modeltokenunitprice/list',
    method: 'get',
    params: query
  })
}

// 查询模型单位token价格详细
export function getModeltokenunitprice(id) {
  return request({
    url: '/gptbill/modeltokenunitprice/' + id,
    method: 'get'
  })
}

// 新增模型单位token价格
export function addModeltokenunitprice(data) {
  return request({
    url: '/gptbill/modeltokenunitprice',
    method: 'post',
    data: data
  })
}

// 修改模型单位token价格
export function updateModeltokenunitprice(data) {
  return request({
    url: '/gptbill/modeltokenunitprice',
    method: 'put',
    data: data
  })
}

// 删除模型单位token价格
export function delModeltokenunitprice(id) {
  return request({
    url: '/gptbill/modeltokenunitprice/' + id,
    method: 'delete'
  })
}
