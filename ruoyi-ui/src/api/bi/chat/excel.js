import request from "@/utils/request";

// /chat-bi-file/upload
export function uploadFile(data){
  return request({
    url: `/chat-bi-file/upload`,
    method: "post",
    data
  });
}

// /chat-bi-file/save
export function saveFile(data){
  return request({
    url: `/chat-bi-file/save`,
    method: "post",
    data
  });
}

// /chat-bi-file/detail/{id}
export function detailFile(id){
  return request({
    url: `/chat-bi-file/detail/${id}`,
    method: "get"
  });
}

export function pageList(params,data){
  return request({
    url: `/chat-bi-file/page-list`,
    method: 'POST',
    params,
    data
  })
}


export function deletedTableIds(data){
  return request({
    url: `/chat-bi-file/batch-delete`,
    method:'POST',
    data
  })
}
