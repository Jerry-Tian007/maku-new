package com.ruoyi.common.i18n;


public enum ReturnMessageEnum {

    kb_file_export_title("kb.file.export.title"),
    RETURN_SUCCESS("return.success"),
    RETURN_FAILED("return.fail"),
    user_jcaptcha_error("user.jcaptcha.error"),
    user_jcaptcha_expire("user.jcaptcha.expire"),
    repeated_ubmission("system.repeated.submission"),
    current_limiting("system.current.limiting"),
    vector_delete_fail("vector.delete.fail"),
    vector_repeated_error("vector.repeated.error"),
    generalization_q_a_deleted("generalization.q.a.deleted"),
    q_a_deleted("q.a.deleted"),
    file_repeated_extract("file.repeated.extract"),
    hot_import_not_null("hot.import.not.null"),
    sensitive_template_name("sensitive.template.name"),
    repeated_delete("repeated.delete"),

    file_name_invalid("file.name.invalid"),
    download_failed("download.failed"),
    file_upload_cannot_be_empty("file.upload.cannot.be.empty"),
    file_name_cannot_be_empty("file.name.cannot.be.empty"),
    file_upload_failed("file.upload.failed"),
    file_parsing_failed("file.parsing.failed"),
    file_extension_cannot_be_empty("file.extension.cannot.be.empty"),
    failed_to_save_the_database("failed.to.save.the.database"),
    please_turn_on_the_storage_media("please.turn.on.the.storage.media"),
    private_domain_error_log_data("private.domain.error.log.data"),
    failed_to_modify_the_file("failed.to.modify.the.file"),
    batch_edit_files_failed("batch.edit.files.failed"),
    failed_to_delete_file("failed.to.delete.file"),
    the_document_is_being_trained_cancellation_failed("the.document.is.being.trained.cancellation.failed"),
    duplicate_filename_please_rename_the_file_or_choose_to_overwrite_the_upload("duplicate.filename.please.rename.the.file.or.choose.to.overwrite.the.upload"),
    the_knowledge_base_name_already_exists_modification_failed("the.knowledge.base.name.already.exists.modification.failed."),
    failed_to_modify_the_knowledge_base("failed.to.modify.the.knowledge.base"),
    failed_to_delete_knowledge_base("failed.to.delete.knowledge.base"),
    failed_to_add_knowledge_base("failed.to.add.knowledge.base"),
    abnormal_gpt("abnormal.gpt"),
    sql_statement_anomaly("sql.statement.anomaly"),
    service_exception("service.exception"),
    add_new_exception("add.new.exception"),
    delete_anomaly("delete.anomaly"),
    modify_exception("modify.exception"),
    query_exception("query.exception"),
    duplicate_anomaly("duplicate.anomaly"),
    other_abnormal("other.abnormal"),
    permission_exception("permission.exception"),
    missing_parameter("missing.parameter"),
    parameter_missing_disabled("parameter.missing.disabled"),
    knowledge_base_question_and_answer_records_data("knowledge.base.question.and.answer.records.data"),
    solved("solved"),
    unsolved("unsolved"),
    accurate("accurate"),
    inaccurate("inaccurate"),
    unrated("unrated"),
    please_select_the_data_to_delete("please.select.the.data.to.delete"),
    hello("hello"),
    what_is_your_name("what.is.your.name"),
    how_old_are_you("how.old.are.you"),
    are_you_male_or_female("are.you.male.or.female"),
    are_you_an_artificial_intelligence("are.you.an.artificial.intelligence"),
    are_you_a_robot("are.you.a.robot"),
    who_are_you("who.are.you"),
    what_are_you_doing("what.are.you.doing"),
    do_you_speak_english("do.you.speak.english"),
    can_you_speak_chinese("can.you.speak.chinese"),
    do_you_speak_french("do.you.speak.french"),
    do_you_speak_german("do.you.speak.german"),
    can_you_speak_japanese("can.you.speak.japanese"),
    upload_success("upload.success");











    /**
     * 返回的国际化key信息
     */
    private String key;


    ReturnMessageEnum(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return i18nMessageUtil.get(key, LanguageContextHolder.getUserLanguage());
    }

    /**
     * 替换占位符中的参数
     *
     * @param params 需要替换的参数值 长度可变
     */
    public String setAndToString(Object... params) {
        return i18nMessageUtil.get(key, params, LanguageContextHolder.getUserLanguage());
    }

}
