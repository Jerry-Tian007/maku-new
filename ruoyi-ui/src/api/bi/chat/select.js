import request from "@/utils/request";

// /chat-bi-data-source-table/select-list
export function selectDataSource(data){
  return request({
    url: `/chat-bi-data-source/select-list`,
    method: "post",
    data
  });
}

// /chat-bi-q-a-record-session/select-list
export function biSessionList(data){
  return request({
    url: `/chat-bi-q-a-record-session/select-list`,
    method: "post",
    data
  })
}

// /chat-bi-q-a-record-session/history
export function biSessionHistory(sessionId){
  return request({
    url: `/chat-bi-q-a-record-session/history`,
    method: "post",
    data: {
      "sessionId": sessionId
    }
  })
}

// /chat-bi-q-a-record-session/delete/{id}
export function deleteBISession(id){
  return request({
    url: `/chat-bi-q-a-record-session/delete/${id}`,
    method: "get"
  })
}
