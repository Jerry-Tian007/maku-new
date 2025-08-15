import request from "@/utils/request";

// /kb/qa-record/list
export function listKBHistory(query, data) {
  return request({
    url: "/kb/qa-record/list",
    method: "post",
    params: query,
    data
  });
}

// /kb/qa-record/list24
export function listMyKBHistory(data) {
  return request({
    url: "/kb/qa-record/list24",
    method: "post",
    data
  });
}

// /kb/qa-record/accuracy-or-not
export function accuracyOrNot(data) {
  return request({
    url: "/kb/qa-record/accuracy-or-not",
    method: "post",
    data
  });
}
