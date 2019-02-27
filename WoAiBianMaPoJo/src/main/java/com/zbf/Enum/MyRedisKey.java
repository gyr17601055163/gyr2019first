package com.zbf.Enum;

public enum MyRedisKey {

    TIKU("tiku"),SHIJUAN("shijuan");

    private String key;

    private MyRedisKey(String key){
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
