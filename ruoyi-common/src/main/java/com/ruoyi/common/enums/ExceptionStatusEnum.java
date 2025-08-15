package com.ruoyi.common.enums;

import com.ruoyi.common.i18n.ReturnMessageEnum;

/**
 * 异常状态
 *
 * @author ruoyi
 */
public enum ExceptionStatusEnum {

    /**
     * 30 数据库异常
     * 40 gpt异常
     * 50 系统异常
     * 60 其他异常
     */

    BAD_SQL_GRAMMAR("30001", ReturnMessageEnum.sql_statement_anomaly.toString()),
    DISABLE("40001", ReturnMessageEnum.abnormal_gpt.toString()),
    SERVICE("50001", ReturnMessageEnum.service_exception.toString()),
    ADD("50002", ReturnMessageEnum.add_new_exception.toString()),
    DELETED("50003", ReturnMessageEnum.delete_anomaly.toString()),

    UPDATE("50004", ReturnMessageEnum.modify_exception.toString()),

    SELECT("50005", ReturnMessageEnum.query_exception.toString()),
    REPEAT("50006", ReturnMessageEnum.duplicate_anomaly.toString()),
    OTHER("60000", ReturnMessageEnum.other_abnormal.toString()),
    PERMISSIONS_ERROR("70000", ReturnMessageEnum.permission_exception.toString()),
    PARAMETER_ANOMALY("80000", ReturnMessageEnum.missing_parameter.toString()),
    PARAMETER_ANOMALY_FORBIDDEN("80001", ReturnMessageEnum.parameter_missing_disabled.toString());

    private final String code;
    private final String info;

    ExceptionStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
