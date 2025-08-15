import request from "@/utils/request";

// /model/model-resources/getSelect
export function getModelSelect() {
  return request({
    url: "/model/model-resources/getSelect",
    method: "get"
  });
}

// /model/model-resource-template/getSelect
export function getModelTemplateSelect() {
  return request({
    url: "/model/model-resource-template/getSelect",
    method: "get"
  });
}
