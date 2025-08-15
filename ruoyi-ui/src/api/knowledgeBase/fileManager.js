import request from "@/utils/request";

// /kb/file/list
export function listFile(query, data) {
  return request({
    url: "/kb/file/list",
    method: "post",
    params: query,
    data
  });
}

// /kb/file/{id}
export function getFileInfo(id) {
  return request({
    url: "/kb/file/" + id,
    method: "get"
  });
}

// /kb/file/edit
export function editFile(data) {
  return request({
    url: "/kb/file/edit",
    method: "post",
    data
  });
}

// /kb/file/levelEdit - 新增level编辑接口
export function editLevel(data) {
  return request({
    url: "/kb/file/editBookLevel",
    method: "post",
    data
  });
}

// /kb/file/remove/{id} post
export function removeFile(id) {
  return request({
    url: "/kb/file/remove/" + id,
    method: "post"
  });
}

// /kb/file-common/upload-file
export function uploadFile(query, data) {
  return request({
    url: "/kb/file-common/upload-file",
    method: "post",
    params: query,
    data,
    headers: {
      repeatSubmit: false
    }
  });
}

// /kb/file-common/file-download?fileName=&delete=true get
export function downloadFile(query) {
  return request({
    url: "/kb/file-common/file-download",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// /kb/file/train
export function trainFile(ids) {
  return request({
    url: "/kb/file/train",
    method: "post",
    data: ids
  });
}

// /kb/file/cancel-training/{id}
export function cancelTraining(id) {
  return request({
    url: "/kb/file/cancel-training/" + id,
    method: "post"
  });
}


export function updateFileTrain(data) {
  return request({
    url: "/kb/file/updateTrain",
    method: "post",
    data
  });
}

// /kb/file/batcheditstatus
export function batchEditStatus(data) {
  return request({
    url: "/kb/file/batchEditStatus",
    method: "post",
    data
  });
}
