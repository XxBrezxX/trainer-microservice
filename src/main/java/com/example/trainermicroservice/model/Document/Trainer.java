package com.example.trainermicroservice.model.Document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "trainer")
public class Trainer {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean status;
    private Set<YearDocument> yearsList = new HashSet<>();
}
