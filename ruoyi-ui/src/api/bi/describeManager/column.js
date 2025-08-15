import request from "@/utils/request";

export function pageListColumn(params,data) {
  return request({
    url: `/chat-bi-data-source-table-field/page-list`,
    method: "post",
    params,
    data
  });
}

export function addColumn(data) {
  return request({
    url: `/chat-bi-data-source-table-field/add`,
    method: "post",
    data
  });
}

export function updateColumn(data) {
  return request({
    url: `/chat-bi-data-source-table-field/edit`,
    method: "post",
    data
  });
}

export function deleteColumn(id) {
  return request({
    url: `/chat-bi-data-source-table-field/delete/${id}`,
    method: "get",
  });
}


export function findSelectList(data){
  return request({
    url: `/chat-bi-data-source/select-list`,
    method: 'POST',
    data
  })
}


export function findTableSelectData(data){
  return request({
    url: `/chat-bi-data-source-table/select-list`,
    method: 'POST',
    data
  })
}

export function syncTableSelect(data){
  return request({
    url: `/chat-bi-data-source-table-field/intelligent/filling`,
    method: 'POST',
    data
  })
}

export function deletedTableIds(data){
  return request({
    url: `/chat-bi-data-source-table-field/batch-delete`,
    method:'POST',
    data
  })
}
