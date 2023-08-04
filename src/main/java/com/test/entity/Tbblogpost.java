package com.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "TBBLOGPOST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Tbblogpost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer id;

    @Column(name = "TITLE")
    public String title;

    @Column(name = "BODY")
    public String body;

    @Column(name = "AUTHOR")
    public String author;

}
