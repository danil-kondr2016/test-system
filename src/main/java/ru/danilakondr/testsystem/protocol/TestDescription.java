package ru.danilakondr.testsystem.protocol;

import lombok.Data;

import java.util.List;

@Data
public class TestDescription {
    String name;
    List<Long> questions;
}
