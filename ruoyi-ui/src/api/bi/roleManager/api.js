import request from "@/utils/request";


export function rolePageList(params, data) {
  return request({
    url: `/chat-bi-role/page-list`,
    method: "post",
    params,
    data
  });
}

export function addRole(data) {
  return request({
    url: `/chat-bi-role/add`,
    method: "post",
    data
  });
}
export function updateRole(data) {
  return request({
    url: `/chat-bi-role/edit`,
    method: "post",
    data
  });
}
export function deletedRole(id) {
  return request({
    url: `/chat-bi-role/delete/${id}`,
    method: "get",
  });
}

export function findDept(){
  return request({
    url: `/system/user/deptTree`,
    method: 'get'
  })
}

export function findSelect(data){
  return request({
    url: `/chat-bi-role/select-list`,
    method: 'POST',
    data
  })
}


export function findItem(id){
  return request({
    url: `/chat-bi-role/detail/${id}`,
    method: 'get'
  })
}

export function findRoleData(data){
  return request({
    url: '/system/user/select-list',
    method: 'POST',
    data
  })
}

export function deletedTableIds(data){
  return request({
    url: `/chat-bi-role/batch-delete`,
    method:'POST',
    data
  })
}
