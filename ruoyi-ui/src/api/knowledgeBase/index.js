import request from "@/utils/request";

// /kb/qa/chat
export function chatKB(data) {
  return request({
    url: "/kb/qa/chat",
    method: "post",
    data
  });
}
