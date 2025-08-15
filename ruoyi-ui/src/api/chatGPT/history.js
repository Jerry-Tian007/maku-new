import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function listHistory(query) {
  return request({
    url: '/gpt/chatgptrecord/list',
    method: 'get',
    params: query
  })
}

// 查询历史
export function addHistory(data) {
  return request({
    url: '/gpt/chatgptrecord',
    method: 'post',
    data: data
  })
}

// 修改历史对话
export function updateHistory(data) {
  return request({
    url: '/gpt/chatgptrecord',
    method: 'put',
    data: data
  })
}
