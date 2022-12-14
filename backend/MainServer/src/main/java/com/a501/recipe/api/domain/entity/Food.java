package com.a501.recipe.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food")
public class Food extends BaseEntity{

    private String name;

    private Float weight;

    @OneToOne
    @JoinColumn(name="nutrient_id")
    private Nutrient nutrient;

}
