import request from '@/utils/request'

// 查询GPT消费概览列表
export function listGptconsumptionoverview (query) {
  return request({
    url: '/gpt/gptconsumptionoverview/list',
    method: 'get',
    params: query
  })
}

// 查询GPT消费概览详细
export function getGptconsumptionoverview (id) {
  return request({
    url: '/gpt/gptconsumptionoverview/' + id,
    method: 'get'
  })
}

// 新增GPT消费概览
export function addGptconsumptionoverview (data) {
  return request({
    url: '/gpt/gptconsumptionoverview',
    method: 'post',
    data: data
  })
}

// 修改GPT消费概览
export function updateGptconsumptionoverview (data) {
  return request({
    url: '/gpt/gptconsumptionoverview',
    method: 'put',
    data: data
  })
}

// 删除GPT消费概览
export function delGptconsumptionoverview (id) {
  return request({
    url: '/gpt/gptconsumptionoverview/' + id,
    method: 'delete'
  })
}

// 生成账单
export function generateBill (data) {
  return request({
    url: '/gpt/gptconsumptionoverview/generate-bill',
    method: 'post',
    data: data
  })
}
// 查询GPT消费概览列表
export function overviewBill (query) {
  return request({
    url: '/gpt/gptconsumptionoverview/total-cost',
    method: 'get',
    params: query
  })
}
// 查询GPT用户个人消费情况
export function overviewPersonalBill (query) {
  return request({
    url: '/gpt/gptconsumptionoverview/user/total-cost',
    method: 'get',
    params: query
  })
}

// 调整个人消费额度
export function updatePersonalMax (data) {
  return request({
    url: '/gpt/gptconsumptionoverview/update/maxConsumptionAmount',
    method: 'post',
    data: data
  })
}

