import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function chatQuestion(url,data) {
  return request({
    url: '/ny/'+url,
    method: 'post',
    headers: {
      repeatSubmit: false,
    },
    data
  })
}

export function wxyyQuestion(data) {
  return request({
    url: '/ny/wxyy',
    method: 'post',
    headers: {
      repeatSubmit: false,
    },
    data
  })
}

export function xfxhQuestion(data) {
  return request({
    url: '/ny/xfxh',
    method: 'post',
    headers: {
      repeatSubmit: false,
    },
    data
  })
}

// 查询session列表
export function listSession(query) {
  return request({
    url: '/chat/history/list',
    method: 'get',
    params: query
  })
}
// addSession
export function addSession(data) {
  return request({
    url: '/chat/history',
    method: 'post',
    data
  })
}
// update
export function updateSession(data) {
  return request({
    url: '/chat/history',
    method: 'put',
    data: data
  })
}
// delete
export function delSession(id) {
  return request({
    url: '/chat/history/'+id,
    method: 'delete'
  })
}
