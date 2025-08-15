import request from "@/utils/request";

// /kb/authorization/list
export function listKBAuth(query, data) {
  return request({
    url: "/kb/authorization/list",
    method: "post",
    params: query,
    data
  });
}


// /kb/authorization/getKbListByRoleId/{roleId}
export function getKbListByRoleId(roleId) {
  return request({
    url: "/kb/authorization/getKbListByRoleId/" + roleId,
    method: "get"
  });
}

// /kb/authorization/getSelect
export function getKBSelect() {
  return request({
    url: "/kb/authorization/getSelect",
    method: "post",
  })
}

// /kb/authorization/add
export function addKBAuth(data) {
  return request({
    url: "/kb/authorization/add",
    method: "post",
    data: data
  });
}
