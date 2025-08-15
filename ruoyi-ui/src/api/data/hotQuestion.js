import request from '@/utils/request'

// 查询热点问题列表
export function listHotQuestion(query) {
  return request({
    url: '/gpt/hotQuestion/list',
    method: 'get',
    params: query
  })
}

export function analyzeHotQuestion(data) {
  return request({
    url: '/gpt/hotQuestion/analyze',
    method: 'post',
    data:data
  })
}

// 查询热点问题详细
export function getHotQuestion(ids) {
  return request({
    url: `/gpt/hotQuestion/`+ ids,
    method: 'get'
  })
}

// 新增热点问题
export function addHotQuestion(data) {
  return request({
    url: '/gpt/hotQuestion',
    method: 'post',
    data: data
  })
}

// 修改热点问题
export function updateHotQuestion(data) {
  return request({
    url: 'gpt/hotQuestion/edit',
    method: 'post',
    data: data
  })
}

// 删除热点问题
export function delHotQuestion(ids) {
  return request({
    url: `/gpt/hotQuestion/delete/${ids}`,
    method: 'post'
  })
}

export function exportQuestion() {
  return request({
    url: '/gpt/hotQuestion/export',
    method: 'post'
  })
}
// 热点问题
export function trainQuestion(data) {
  return request({
    url: `/gpt/hotQuestion/train`,
    method: 'post',
    data
  })
}

export function listMonitor(){
  return request({
    url:'/gpt/hotQuestion/monitor',
    method:'get',
  })
}
