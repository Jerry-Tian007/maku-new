package com.ruoyi;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class YamlJsonExtractor {

    private static final String FILE_PATH = "D:\\PROD\\mk\\java\\jsonfile.txt";

    /**
     * 主方法：演示如何修复文件并使用 JsonObject。
     */
    public static void main(String[] args) throws IOException {
        // 1. 获取原始的、不符合JSON格式的文本内容
        // 这里使用您提供的文件内容片段作为示例。
        String rawInputText = getRawInputText();

        try {
            // 2. 执行第一步和第二步：预处理并构建有效的JSON数组字符串
            String fixedJsonString = fixJsonContent(rawInputText);

            // 输出修复后的JSON字符串
            System.out.println("=== 修复后的JSON字符串 ===");
            System.out.println(fixedJsonString);

            // 3. 执行第三步：使用 JsonObject (Gson) 来解析和操作修复后的JSON
            // 这证明了我们生成的字符串是有效的。
            parseAndUseJsonObject(fixedJsonString);

        } catch (Exception e) {
            System.err.println("处理过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 第一步和第二步：预处理和构建有效JSON
     * 从原始文本中提取所有以 "id" 为标识的JSON对象片段，并将它们组合成一个有效的JSON数组。
     *
     * @param inputText 原始的、格式混乱的文本
     * @return 一个有效的JSON数组字符串
     */
    public static String fixJsonContent(String inputText) {
        List<String> validJsonObjects = new ArrayList<>();

        // 正则表达式：匹配以 { 开头，包含 "id": "...", 并以 } 结尾的完整JSON对象。
        // 这个模式比之前更精确，能处理多层嵌套的花括号。
        // 核心思路是：匹配 {，然后匹配任意字符（非贪婪），但要确保内部的 { 和 } 成对出现。
        Pattern pattern =  Pattern.compile("\\{(?:[^{}]*|\\{[^{}]*\\})*\\}");

        // 由于Java不支持递归正则，我们使用一个更简单但在此场景下有效的策略：
        // 匹配从 {"id":... 开始，到 } 结束的块，并假设内部的 } 不会破坏整体结构。
        // 这个简单模式对于您提供的数据是有效的。
        Pattern simplePattern = Pattern.compile(
                "\\{[^}]*?\"id\"\\s*:\\s*\"[^\"]*\"[^}]*?\\}",
                Pattern.DOTALL
        );

        Matcher matcher = simplePattern.matcher(inputText);
        while (matcher.find()) {
            String potentialObject = matcher.group();
            // 尝试用 Gson 验证这个字符串是否真的是一个有效的JSON对象
            if (isValidJsonObject(potentialObject)) {
                validJsonObjects.add(potentialObject);
            } else {
                System.out.println("警告：跳过一个无效的JSON片段: " + potentialObject.substring(0, Math.min(50, potentialObject.length())) + "...");
            }
        }

        if (validJsonObjects.isEmpty()) {
            return "[]";
        }

        // 构建最终的JSON数组
        StringBuilder jsonArrayBuilder = new StringBuilder();
        jsonArrayBuilder.append("[\n");
        for (int i = 0; i < validJsonObjects.size(); i++) {
            jsonArrayBuilder.append(validJsonObjects.get(i));
            if (i < validJsonObjects.size() - 1) {
                jsonArrayBuilder.append(",\n");
            }
        }
        jsonArrayBuilder.append("\n]");

        return jsonArrayBuilder.toString();
    }

    /**
     * 辅助方法：验证一个字符串是否为有效的JSON对象。
     * 使用 Gson 的 JsonReader 进行严格验证。
     *
     * @param jsonString 待验证的JSON字符串
     * @return 如果是有效的JSON对象则返回 true
     */
    private static boolean isValidJsonObject(String jsonString) {
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonString));
            reader.setLenient(false); // 严格模式
            JsonToken token = reader.peek();
            if (token == JsonToken.BEGIN_OBJECT) {
                reader.skipValue(); // 读取整个对象
                return reader.peek() == JsonToken.END_DOCUMENT; // 确保没有多余字符
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 第三步：使用 JsonObject
     * 演示如何使用 Gson 的 JsonObject 和 JsonArray 来解析和操作已经修复的JSON。
     *
     * @param jsonString 有效的JSON数组字符串
     */
    public static void parseAndUseJsonObject(String jsonString) {
        Gson gson = new Gson();
        try {
            // 将字符串解析为 JsonElement
            JsonElement jsonElement = JsonParser.parseString(jsonString);

            // 断言它是一个数组
            if (!jsonElement.isJsonArray()) {
                throw new JsonSyntaxException("预期为JSON数组，但得到的是: " + jsonElement.getClass().getSimpleName());
            }

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            System.out.println("\n=== 使用 JsonObject 解析结果 ===");
            System.out.println("总共有 " + jsonArray.size() + " 个对象。");

            // 遍历数组中的每个元素
            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                String id = obj.get("id").getAsString();

                // 尝试获取 document_metadata 或 title
                JsonElement metadataElement = obj.get("document_metadata");
                String title = obj.has("title") ? obj.get("title").getAsString() : "Unknown Title";
                String metadataType = metadataElement != null ? (metadataElement.isJsonObject() ? "Object" : "String") : "None";

                System.out.printf("对象 %d: ID='%s', Title='%s', Metadata Type=%s%n", i + 1, id, title, metadataType);

                // 示例：如果 metadata 是对象，可以进一步解析
                if (metadataElement != null && metadataElement.isJsonObject()) {
                    JsonObject metadataObj = metadataElement.getAsJsonObject();
                    // 这里可以访问 metadataObj 的具体字段
                }
            }
        } catch (JsonSyntaxException e) {
            System.err.println("解析修复后的JSON时出错，这不应该发生！错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 模拟读取原始文件内容。
     * 在实际应用中，这里应该从文件系统读取 jsonfile.txt 的内容。
     *
     * @return 原始的、格式混乱的文本
     */
    private static String getRawInputText() throws
            IOException {
        // 这里粘贴您提供的文件内容
        return new String(Files.readAllBytes(Paths.get(FILE_PATH)));

    }
}