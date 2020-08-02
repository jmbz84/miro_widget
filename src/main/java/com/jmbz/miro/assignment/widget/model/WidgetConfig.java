package com.jmbz.miro.assignment.widget.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WidgetConfig {
    @NotNull
    String endpoint;

    @NotNull
    String method;

    @NotNull
    Integer rpm;
}
