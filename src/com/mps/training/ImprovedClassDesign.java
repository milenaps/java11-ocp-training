package com.mps.training;

import java.math.BigDecimal;

public class ImprovedClassDesign {

    private String name;
    private BigDecimal value;

    public ImprovedClassDesign(String name) {
        this(name, BigDecimal.valueOf(0l));
    }

    public ImprovedClassDesign(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ImprovedClassDesign{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
