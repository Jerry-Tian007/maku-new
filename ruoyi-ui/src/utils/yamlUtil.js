import YAML from 'js-yaml';

// JSON 转 YAML 字符串
export const jsonToYaml = (jsonData) => {
  return YAML.dump(jsonData, {
    lineWidth: -1,       // 禁用自动换行
    noRefs: true,        // 禁止引用重复数据
    skipInvalid: true    // 跳过无效字段
  });
};
