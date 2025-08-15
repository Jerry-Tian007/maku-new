import request from "@/utils/request";

// /model/model-resource-template/list
export function listModelResourceTemplate(query, data) {
  return request({
    url: '/model/model-resource-template/list',
    method: 'post',
    params: query,
    data
  })
}

// /model/model-resource-template post
export function addModelResourceTemplate(data) {
  return request({
    url: '/model/model-resource-template',
    method: 'post',
    data
  })
}

// /model/model-resource-template/update post
export function updateModelResourceTemplate(data) {
  return request({
    url: '/model/model-resource-template/update',
    method: 'post',
    data
  })
}

// /model/model-resource-template/{id} get
export function getModelResourceTemplate(id) {
  return request({
    url: `/model/model-resource-template/${id}`,
    method: 'get'
  })
}

// /model/model-resource-template/delete/{ids} get
export function deleteModelResourceTemplate(ids) {
  return request({
    url: `/model/model-resource-template/delete/${ids}`,
    method: 'get'
  })
}
