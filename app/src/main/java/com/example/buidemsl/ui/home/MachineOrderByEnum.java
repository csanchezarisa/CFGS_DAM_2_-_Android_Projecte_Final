package com.example.buidemsl.ui.home;

import com.example.buidemsl.models.BuidemHelper;

public enum MachineOrderByEnum {
    CLIENT_NAME(BuidemHelper.CLIENT_NOM),
    ZONE("zDescripcio"),
    TOWN(BuidemHelper.MAQUINA_POBLACIO),
    DIRECTION(BuidemHelper.MAQUINA_ADRECA),
    LAST_REV_DATE(BuidemHelper.TABLE_MAQUINA + "." + BuidemHelper.MAQUINA_ULTIMA_REVISIO)
    ;

    public final String label;

    MachineOrderByEnum(String label) {
        this.label = label;
    }
}
