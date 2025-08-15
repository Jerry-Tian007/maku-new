import request from '@/utils/request'

// /model/model-resource-role/update post
export function updateModelResourceRole(data) {
  return request({
    url: '/model/model-resource-role/update',
    method: 'post',
    data
  })
}

// /model/model-resource-role/getModelListByRoleId/{roleId}
export function getModelListByRoleId(roleId) {
  return request({
    url: "/model/model-resource-role/getModelListByRoleId/" + roleId,
    method: "get"
  });
}
