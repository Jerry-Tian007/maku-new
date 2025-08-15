import request from "@/utils/request";

// /model/model-resources/list post
export function listModelResources(query,data) {
  return request({
    url: '/model/model-resources/list',
    method: 'post',
    data,
    params: query
  })
}

// /model/model-resources  post
export function addModelResource(data) {
  return request({
    url: '/model/model-resources',
    method: 'post',
    data
  })
}

// /model/model-resources/update post
export function updateModelResource(data) {
  return request({
    url: '/model/model-resources/update',
    method: 'post',
    data
  })
}

// /model/model-resources/{id} get
export function getModelResource(id) {
  return request({
    url: `/model/model-resources/${id}`,
    method: 'get'
  })
}

// /model/model-resources/delete post
export function deleteModelResource(ids) {
  return request({
    url: `/model/model-resources/delete`,
    method: 'post',
    data: ids
  })
}
