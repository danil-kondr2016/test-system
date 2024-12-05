package ru.danilakondr.testsystem.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestListResponse extends Response {
    List<TestDescription> tests;
}
