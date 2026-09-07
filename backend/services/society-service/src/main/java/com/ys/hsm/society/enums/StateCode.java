package com.ys.hsm.society.enums;

import lombok.Getter;

@Getter
public enum StateCode {

    ANDHRA_PRADESH("Andhra Pradesh", "AP"),
    ARUNACHAL_PRADESH("Arunachal Pradesh", "AR"),
    ASSAM("Assam", "AS"),
    BIHAR("Bihar", "BR"),
    CHHATTISGARH("Chhattisgarh", "CG"),
    GOA("Goa", "GA"),
    GUJARAT("Gujarat", "GJ"),
    HARYANA("Haryana", "HR"),
    HIMACHAL_PRADESH("Himachal Pradesh", "HP"),
    JHARKHAND("Jharkhand", "JH"),
    KARNATAKA("Karnataka", "KA"),
    KERALA("Kerala", "KL"),
    MADHYA_PRADESH("Madhya Pradesh", "MP"),
    MAHARASHTRA("Maharashtra", "MH"),
    MANIPUR("Manipur", "MN"),
    MEGHALAYA("Meghalaya", "ML"),
    MIZORAM("Mizoram", "MZ"),
    NAGALAND("Nagaland", "NL"),
    ODISHA("Odisha", "OD"),
    PUNJAB("Punjab", "PB"),
    RAJASTHAN("Rajasthan", "RJ"),
    SIKKIM("Sikkim", "SK"),
    TAMIL_NADU("Tamil Nadu", "TN"),
    TELANGANA("Telangana", "TS"),
    TRIPURA("Tripura", "TR"),
    UTTAR_PRADESH("Uttar Pradesh", "UP"),
    UTTARAKHAND("Uttarakhand", "UK"),
    WEST_BENGAL("West Bengal", "WB"),
    DELHI("Delhi", "DL");

    private final String stateName;
    private final String code;

    StateCode(String stateName, String code) {
        this.stateName = stateName;
        this.code = code;
    }
}
