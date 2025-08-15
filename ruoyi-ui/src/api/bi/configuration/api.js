import request from "@/utils/request";

export function pageTable(params, data) {
  return request({
    url: `/chat-bi-function-call/page-list`,
    method: 'POST',
    params,
    data
  });
}


export function add(data) {
  return request({
    url: `/chat-bi-function-call/add`,
    method: 'POST',
    data
  });
}

export function findItem(id){
  return request({
    url: `/chat-bi-function-call/detail/${id}`,
    method: 'GET'
  })
}

export function update(data) {
  return request({
    url: `/chat-bi-function-call/edit`,
    method: 'POST',
    data
  });
}

export function deleted(id) {
  return request({
    url: `/chat-bi-function-call/delete/${id}`,
    method: 'GET'
  });
}

export function deleteds(data) {
  return request({
    url: '/chat-bi-function-call/batch-delete',
    method: 'POST',
    data
  })
}

export function authorize(data) {
  return request({
    url: `/chat-bi-role-function/select-list/authorized`,
    method: 'POST',
    data
  });
}

export function noAuthorize(data) {
  return request({
    url: `/chat-bi-role-function/select-list/unauthorized`,
    method: 'POST',
    data
  });
}

export function addAuthorize(data) {
  return request({
    url: `/chat-bi-role-function/add`,
    method: 'POST',
    data
  });
}

export function findDataSource(data) {
  return request({
    url: `/chat-bi-data-source/select-list`,
    method: 'POST',
    data
  })
}

export function functionGenerate(data) {
  return request({
    url: `/chat-bi-function-call/generate`,
    method: 'POST',
    data
  })
}


export function functionType() {
  return request({
    url: `/system/dict/data/type/functioncall_chart_type`,
    method: 'GET',
  })
}

export function functionTest(data) {
  return request({
    url: `/chat-bi-function-call/test`,
    method: 'POST',
    data
  })
}
