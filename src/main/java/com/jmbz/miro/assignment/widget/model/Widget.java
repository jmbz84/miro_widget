package com.jmbz.miro.assignment.widget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

    @Id
    private String id;

    @NotNull
    @Positive(message = "height must greater than zero")
    private Integer height;

    @NotNull
    @Positive(message = "width must greater than zero")
    private Integer width;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    private Integer zIndex;

    private String dateModif;

    @JsonIgnore
    public boolean isInsideBottomLeft(Coordinate area){
        return area.getX() <= x-(width/2) && area.getY() <= y-(height/2);
    }

    @JsonIgnore
    public boolean isInsideTopRight(Coordinate area){
        return area.getX() >= x+(width/2) && area.getY() >= y+(height/2);
    }


}