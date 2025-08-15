import request from "@/utils/request";
// /model/model-api/chat
export function chatQuestion(data) {
  return request({
    url: "/chat/chatgpt/chat",
    method: "post",
    headers: {
      repeatSubmit: false,
    },
    data,
  });
}

// /model/model-resources/authorize/list
export function listBots() {
  return request({
    url: "/model/model-resources/authorize/list",
    method: "get",
  });
}


export function publicPromptList(data, params) {
  return request({
    url: "/kb/public-prompt/page-list",
    method: "post",
    params,
    data
  })
}

export function personalPromptList(data, params) {
  return request({
    url: "/kb/personal-prompt/page-list",
    method: "post",
    params,
    data
  })
}

export function communitySave(data) {
  return request({
    url: "/kb/public-prompt/save",
    method: "post",
    data
  })
}

export function findModelParam(data) {
  return request({
    url: "/kb/user-model-parameter/get/parameter",
    method: "post",
    data
  })
}

export function saveModelParam(data) {
  return request({
    url: "/kb/user-model-parameter/add",
    method: "post",
    data
  })
}

export function addPrompt(data) {
  return request({
    url: "/kb/personal-prompt/add",
    method: "post",
    data
  })
}

export function updatePrompt(data) {
  return request({
    url: "/kb/personal-prompt/edit",
    method: "post",
    data
  })
}

export function deletedPrompt(id) {
  return request({
    url: `/kb/personal-prompt/delete/${id}`,
    method: "get"
  })
}

export function kbManagerSelectPromptList(kbManagerId) {
  let url = '/kb/personal-prompt/kbManagerSelectPromptList';
  if (kbManagerId !== undefined && kbManagerId !== null) {
    url = '/kb/personal-prompt/kbManagerSelectPromptList?kbManagerId='+kbManagerId;
  }
  return request({
    url: url,
    method: 'get'
  })
}


export function releasePrompt(data) {
  return request({
    url: `/kb/personal-prompt/release`,
    method: "post",
    data
  })
}


export function uploadFile(file) {
  const formData = new FormData();


  formData.append('file', file);


  return request({
    url: `/all-in-one/file/upload`,
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    data: formData
  })
}

export function findTokenNum(data) {
  return request({
    endpoint: "gptData",
    url: '/get_token_num',
    method: "post",
    data
  })
}

export function findHistoryItem(data) {
  return request({
    url: `/chat-model-session/session/history`,
    method: "post",
    data
  })
}

export function findHistoryList(data, params) {
  return request({
    url: `/chat-model-session/model-top-session`,
    method: "post",
    data,
    params
  })
}

export function deletedHistoryById(id){
  return request({
    url: `/chat-model-session/delete/${id}`,
    method: "get",
  })
}
