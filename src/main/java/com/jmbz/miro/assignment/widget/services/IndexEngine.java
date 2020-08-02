package com.jmbz.miro.assignment.widget.services;

import com.jmbz.miro.assignment.widget.model.IndexNode;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Component which manages the zIndices
 */
@Component
public class IndexEngine {

    LinkedList<IndexNode> list = new LinkedList<>();


    /**
     * Inserts a Node and assigns the z-Index automatically if null
     *
     * @param indexNode Object to insert
     * @return
     */
    public IndexNode insertNode(IndexNode indexNode){
        //Z index is null insert first and move all
        if(indexNode.getZIndex()==null){
            IndexNode firstNode;
            try {
                firstNode=list.getFirst();
                indexNode.setZIndex(firstNode.getZIndex()-1);
            }catch (NoSuchElementException ex){
                indexNode.setZIndex(0);
            }
            list.addFirst(indexNode);
        }
        else{
            for(int i=0;i<list.size();i++){
                if(list.get(i).getZIndex() >= indexNode.getZIndex()){
                    //i contains the position where the new node should be inserted
                    list.add(i,indexNode);
                    return indexNode;
                }
            }
            list.addLast(indexNode);
        }
        return indexNode;
    }

    public List<IndexNode> getList(){
        return list.subList(0,list.size());
    }

    public boolean zIndexExists(Integer zIndex){
        return list.stream().anyMatch(index -> zIndex==index.getZIndex());
    }

    /**
     *  Updates the values of a node.
     *
     * @param indexNode Object to update
     * @return
     */
    public IndexNode updateNode(IndexNode indexNode) {
        deleteNode(indexNode.getId());
        return insertNode(indexNode);
    }

    public void deleteNode(String id) {
        for(int i=0;i<list.size();i++){
            if(list.get(i).getId().equals(id)){
                //i contains the position where the new node should be inserted
                list.remove(i);
                return;
            }
        }
    }

    public void deleteAll() {
        this.list.clear();
    }
}
