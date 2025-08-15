import request from "@/utils/request";

// /model/model-resources/authorize/list
export function listAuthorize(query, data) {
  return request({
    url: "/model/model-resource-role/platform-list",
    method: "post",
    params: query,
    data
  });
}
