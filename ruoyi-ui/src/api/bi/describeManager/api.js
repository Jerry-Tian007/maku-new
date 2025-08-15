import request from "@/utils/request";


export function pageListChatBiTableData(params,data) {
  return request({
    url: `/chat-bi-data-source-table/page-list`,
    method: "post",
    params,
    data
  });
}


export function deletedPageListById(id){
  return request({
    url: `/chat-bi-data-source-table/delete/${id}`,
    method: 'get'
  })
}

export function updateDescribe(data){
  return request({
    url: `/chat-bi-data-source-table/edit`,
    method: 'POST',
    data
  })
}


export function findSelectList(data){
  return request({
    url: `/chat-bi-data-source/select-list`,
    method: 'POST',
    data
  })
}


export function syncTableSelect(data){
  return request({
    url: `/chat-bi-data-source-table/intelligent/filling`,
    method: 'POST',
    data
  })
}

export function deleteTableSelectData(data){
  return request({
    url: `/chat-bi-data-source-table/batch-delete`,
    method: 'POST',
    data
  })
}
