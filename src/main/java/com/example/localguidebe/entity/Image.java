package com.example.localguidebe.entity;

import com.example.localguidebe.enums.AssociateName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_link",nullable = false)
    private String imageLink;


     @Column(name="associate_name")
     @Enumerated(EnumType.STRING)
      private AssociateName associateName;

     @Column(name = "associate_id")
        private Long associateId;
//    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "tour_id")
//    private Tour tour;
//
//    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id")
//
//    private Location location;
}
