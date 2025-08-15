import request from "@/utils/request";

// /chat-bi-data-source/page-list post
export function pageListChatBiDataSource(params,data) {
  return request({
    url: `/chat-bi-data-source/page-list`,
    method: "post",
    params,
    data
  });
}


export function addChatBiDataSource(data) {
  return request({
    url: `/chat-bi-data-source/add`,
    method: "post",
    data
  });
}

export function databaseTest(data) {
  return request({
    url: `/chat-bi-data-source/test-connect`,
    method: "post",
    data
  });
}


export function updateChatBiDataSource(data) {
  return request({
    url: `/chat-bi-data-source/edit`,
    method: "post",
    data
  });
}


export function deleteChatBiDataSource(id) {
  return request({
    url: `/chat-bi-data-source/delete/${id}`,
    method: "get",
  });
}


export function syncDatabase(id){
  return request({
    url: `/chat-bi-data-source/data-sync/${id}`,
    method: 'get'
  });
}

export function deletedTableIds(data){
  return request({
    url: `/chat-bi-data-source/batch-delete`,
    method:'POST',
    data
  })
}

