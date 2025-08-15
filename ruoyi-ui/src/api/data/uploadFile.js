import request from "@/utils/request";

// 查询文件列表
export function listFile(query) {
  return request({
    url: '/gpt/userblob/list',
    method: 'get',
    params: query,
  })
}

// 修改文件
export function editFile(data) {
  return request({
    url: `/gpt/userblob/edit`,
    method: 'post',
    data
  })
}
// 删除文件
export function delFile(ids) {
  return request({
    url: `/gpt/userblob/${ids}`,
    method: 'post',
  })
}

//导出
export function  exportFile(){
  return request({
    url:'/gpt/userblob/export',
    method:'post'
  })
}

// 更新文件
// export function updateFile(data) {
//   return request({
//     url: `/gpt/userblob/`,
//     method: 'post',
//     data
//   })
// }

// 文件上传
export function fileUpload(data) {
  return request({
    url: '/blob/upload/upload',
    method: 'post',
    data,
    headers: {
      repeatSubmit: false
    }
  })
}

export function checkIsTrained() {
  return request({
    url: '/gpt/userblob/isTrained',
    method: 'get',
  })
}

export function Trained(data) {
  return request({
    url: '/blob/upload/train',
    method: 'post',
    data
  })
}
export function checkIsDomesticTrained() {
  return request({
    url: '/gpt/userblob/isDomesticTrained',
    method: 'get',
  })
}

// 文件下载
export function fileDownloadWord(url) {
  return request({
    url: '/with/oa/file/wordToPdf?fileUrl='+url,
    method: 'get',
    responseType: 'blob'
  })
}

// 文件下载
export function fileDownloadPPT(url) {
  return request({
    url: '/with/oa/file/pptToPdf?fileUrl='+url,
    method: 'get',
    responseType: 'blob'
  })
}
