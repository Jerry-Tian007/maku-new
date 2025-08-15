import request from '@/utils/request'

// 查询GPT账单列表
export function listBill(query) {
  return request({
    url: '/bill/list',
    method: 'get',
    params: query
  })
}

// 查询GPT账单详细
export function getBill(id) {
  return request({
    url: '/bill/' + id,
    method: 'get'
  })
}

// 新增GPT账单
export function addBill(data) {
  return request({
    url: '/bill/bill',
    method: 'post',
    data: data
  })
}

// 修改GPT账单
export function updateBill(data) {
  return request({
    url: '/bill/bill',
    method: 'put',
    data: data
  })
}

// 删除GPT账单
export function delBill(id) {
  return request({
    url: '/bill/bill/' + id,
    method: 'delete'
  })
}
