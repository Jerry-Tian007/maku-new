import request from "@/utils/request";

// /kb/manager/list
export function listKB(query,data) {
  return request({
    url: "/kb/manager/list",
    method: "post",
    params: query,
    data,
    headers: {
      repeatSubmit: false
    }
  });
}

// /kb/manager/{id}
export function getKB(id) {
  return request({
    url: "/kb/manager/" + id,
    method: "get"
  });
}

// /kb/manager/add
export function addKB(data) {
  return request({
    url: "/kb/manager/add",
    method: "post",
    data: data
  });
}

// /kb/manager/edit
export function editKB(data) {
  return request({
    url: "/kb/manager/edit",
    method: "post",
    data: data
  });
}

// /kb/manager/remove/{id}
export function delKB(id) {
  return request({
    url: "/kb/manager/remove/" + id,
    method: "post"
  });
}

export function delMutiKB(ids){
  return request({
    url: "/kb/manager/removeBath",
    method: "post",
    data: ids
  });
}

// /kb/manager/listAll
export function listAllKB() {
  return request({
    url: "/kb/manager/listAll",
    method: "get"
  });
}
