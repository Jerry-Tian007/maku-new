import auth from "../plugins/auth";

export function checkPermission() {
  if(auth.hasPermi('gpt:kbqarecord:edit')){
    return 'ok';
  } else {
    return 'no';
  }
}
