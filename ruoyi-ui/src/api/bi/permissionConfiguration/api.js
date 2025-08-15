import request from "@/utils/request";


export function findDept() {
  return request({
    url: `/system/user/deptTree`,
    method: 'GET'
  })
}

export function findTable(chatBiRoleId, params) {
  return request({
    url: `/chat-bi-permission-configuration/table/page-list/authorized/${chatBiRoleId}`,
    method: 'GET',
    params
  })
}

export function findPermissionRole(params, data) {
  return request({
    url: `/chat-bi-permission-configuration/table/page-list/unauthorized`,
    method: 'POST',
    params,
    data
  })
}

export function rolePageList(params, data) {
  return request({
    url: `/chat-bi-role/page-list`,
    method: "POST",
    params,
    data
  });
}

export function roleList(data) {
  return request({
    url: `/chat-bi-role/page-list`,
    method: "POST",
    data
  });
}


export function findSelectList(data) {
  return request({
    url: `/chat-bi-data-source/select-list`,
    method: 'POST',
    data
  })
}

export function add(data) {
  return request({
    url: `/chat-bi-permission-configuration/add`,
    method: 'POST',
    data
  })
}

export function deletedTableIds(data) {
  return request({
    url: `/chat-bi-permission-configuration/batch-delete`,
    method: 'POST',
    data
  })
}

export function columnAuth(data) {
  return request({
    url: `/chat-bi-role-field/select-list/authorized`,
    method: 'POST',
    data
  });
}

export function columnUnAuth(data) {
  return request({
    url: `/chat-bi-role-field/select-list/unauthorized`,
    method: 'POST',
    data
  });
}

export function addColumnAuth(data) {
  return request({
    url: `/chat-bi-role-field/add`,
    method: 'POST',
    data
  });
}

export function findAllRole(data) {
  return request({
    url: `/chat-bi-data-source-table-field/select-list`,
    method: 'POST',
    data
  });
}
