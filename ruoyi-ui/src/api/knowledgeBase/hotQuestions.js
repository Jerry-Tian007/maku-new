import request from "@/utils/request";

// /gpt/hotQuestion/randomQuestionList
export function randomQuestionList() {
  return request({
    url: "/gpt/hotQuestion/randomQuestionList",
    method: "get"
  });
}
