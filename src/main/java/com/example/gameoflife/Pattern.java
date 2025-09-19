package com.example.gameoflife;

public enum Pattern {
    BLINKER("""
        ...
        OOO
        ...
        """),
    
    GLIDER("""
        .O.
        ..O
        OOO
        """),
    
    BLOCK("""
        OO
        OO
        """),
    
    BEACON("""
        OO..
        OO..
        ..OO
        ..OO
        """),
    
    TOAD("""
        .OOO
        OOO.
        """),
    
    PULSAR("""
        ..OOO...OOO..
        .............
        O....O.O....O
        O....O.O....O
        O....O.O....O
        ..OOO...OOO..
        .............
        ..OOO...OOO..
        O....O.O....O
        O....O.O....O
        O....O.O....O
        .............
        ..OOO...OOO..
        """),
    
    GOSPER_GLIDER_GUN("""
        ........................O...........
        ......................O.O...........
        ............OO......OO............OO
        ...........O...O....OO............OO
        OO........O.....O...OO..............
        OO........O...O.OO....O.O...........
        ..........O.....O.......O...........
        ...........O...O....................
        ............OO......................
        """);
    
    private final String pattern;
    
    Pattern(String pattern) {
        this.pattern = pattern;
    }
    
    public Grid toGrid() {
        return new Grid(pattern);
    }
    
    public Grid toGrid(BoundaryCondition boundary) {
        return new Grid(pattern, boundary);
    }
}
