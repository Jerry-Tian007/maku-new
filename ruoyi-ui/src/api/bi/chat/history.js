import request from "@/utils/request";

// /chat-bi-q-a-record/accuracy
export function biAccuracy(data){
  return request({
    url: `/chat-bi-q-a-record/accuracy`,
    method: "post",
    data
  })
}
