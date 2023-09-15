package com.akka.test.message.enums;

public enum OperationSymbol {

    GT,//大于
    GTorEQ,//大于等于
    LT,//小于
    LTorQE,//小于等于
    EQ,//等于
    noEQ,
    ;//不等于

    public String getZhDescribe() {
        if (GT == this) {
            return "大于";
        } else if (GTorEQ == this) {
            return "大于等于";
        } else if (LT == this) {
            return "小于";
        } else if (LTorQE == this) {
            return "小于等于";
        } else if (EQ == this) {
            return "等于";
        } else if (noEQ == this) {
            return "不等于";
        } else {
            return "未知";
        }
    }

    public static OperationSymbol getBySymbol(String optSymbol){
        OperationSymbol[] values = OperationSymbol.values();
        for (OperationSymbol value : values){
            if(String.valueOf(value).equals(optSymbol)){
                return value;
            }
        }
        return null;
    }
}
