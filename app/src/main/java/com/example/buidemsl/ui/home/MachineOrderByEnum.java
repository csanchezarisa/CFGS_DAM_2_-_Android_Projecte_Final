package com.example.buidemsl.ui.home;

import com.example.buidemsl.models.BuidemHelper;

public enum MachineOrderByEnum {
    CLIENT_NAME(BuidemHelper.TABLE_CLIENT + "." + BuidemHelper.CLIENT_NOM),
    LAST_REV_DATE(BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ULTIMA_REVISIO)
    ;

    public final String label;

    MachineOrderByEnum(String label) {
        this.label = label;
    }
}
