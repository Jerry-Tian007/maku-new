import request from "@/utils/request";

// /gpt/user-kb-history/list
export function listUserSession() {
  return request({
    url: "/kb/user-kb-history/list",
    method: "get"
  });
}

// /kb/qa-record/get-history-by-session-id/{sessionId}
export function getHistoryBySessionId(sessionId) {
  return request({
    url: "/kb/qa-record/get-history-by-session-id/"+sessionId,
    method: "get"
  });
}

// /gpt/user-kb-history/remove/{id}
export function removeUserSession(id) {
  return request({
    url: "/kb/user-kb-history/remove/"+id,
    method: "get"
  });
}

// /kb/qa-record/get-session-ids-by-user-id/{userId}
export function getSessionIdsByUserId(userId) {
  return request({
    url: "/kb/qa-record/get-session-ids-by-user-id/"+userId,
    method: "get"
  });
}
