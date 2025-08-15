<template>
    <div class="markdown-ast-container" v-html="content"></div>
</template>

<script>

export default {
    name: 'MarkdownASTProcessor',
    props: {
        content: {
            type: String,
            required: true
        }
    }
}
</script>

<style scoped>
.markdown-ast-container {
    margin: 0 auto;
}

/* 深度样式覆盖 */
.markdown-ast-container>>>table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
    border-radius: 12px;
    overflow: hidden;
}

.markdown-ast-container>>>th {
    background-color: #f5f7fa;
    border: 1px solid #e4e7ed;
    padding: 12px;
    text-align: left;
}

.markdown-ast-container>>>td {
    border: 1px solid #e4e7ed;
    padding: 8px 12px;
}

.markdown-ast-container>>>pre {
    background-color: #f8f8f8;
    padding: 1rem;
    border-radius: 4px;
    overflow-x: auto;
    white-space: pre-wrap;
    word-break: break-word;
}
</style>