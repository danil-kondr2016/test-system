package ru.danilakondr.testsystem.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(optional = false)
    private User user;

    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="test")
    private List<Question> questions;
}
