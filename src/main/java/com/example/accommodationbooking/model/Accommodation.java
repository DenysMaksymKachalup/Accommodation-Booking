package com.example.accommodationbooking.model;

import com.example.accommodationbooking.model.enumaration.TypeBuilding;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE accommodations SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@Table(name = "accommodations")
@RequiredArgsConstructor
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_building", nullable = false, columnDefinition = "VARCHAR(50)")
    private TypeBuilding type;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "address_street",nullable = false)),
            @AttributeOverride(name = "city",
                    column = @Column(name = "address_city",nullable = false))
    })
    private Address address;

    @Column(name = "size", nullable = false)
    private String size;

    @ElementCollection
    @CollectionTable(name = "accommodations_amenities")
    private List<String> amenities = new ArrayList<>();

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Column(name = "availability", nullable = false)
    private Integer availability;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public Accommodation(Long id) {
        this.id = id;
    }
}



