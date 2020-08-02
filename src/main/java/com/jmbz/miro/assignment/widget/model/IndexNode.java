package com.jmbz.miro.assignment.widget.model;

import lombok.Data;

import java.util.UUID;

@Data
public class IndexNode {

    private String  id;
    private Integer zIndex;

    public IndexNode(String id,Integer zIndex) {
        this.zIndex=zIndex;
        if(id==null){
            this.id=UUID.randomUUID().toString();
        }else{
            this.id=id;
        }
    }
}
