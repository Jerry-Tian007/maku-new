import request from "@/utils/request";

// /kb/prompt/list
export function listQueryPrompts(query,data) {
  return request({
    url: "/kb/personal-prompt/query-prompt-list",
    method: "post",
    params: query,
    data,
    headers: {
      repeatSubmit: false
    }
  });
}

// /kb/prompt/listAll
export function listAllKB() {
  return request({
    url: "/kb/prompt/listAll",
    method: "get"
  });
}
