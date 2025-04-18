package com.STDev.bbang.domain.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bread {

    CIABATTA(1,"치아바타"),
    FOCACCIA(2, "포카치아"),
    SOURDOUGH(3, "사워도우");

    private final int breadId;
    private final String name;
}
