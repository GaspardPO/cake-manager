package com.waracle.cakemgr;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "Cakes")
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1321265956240974641L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    @JsonProperty("desc")
    private String description;

    @Column(nullable = false, length = 300)
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}