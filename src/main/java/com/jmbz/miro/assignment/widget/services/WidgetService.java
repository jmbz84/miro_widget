package com.jmbz.miro.assignment.widget.services;


import com.jmbz.miro.assignment.widget.model.Coordinate;
import com.jmbz.miro.assignment.widget.model.IndexNode;
import com.jmbz.miro.assignment.widget.model.Widget;
import com.jmbz.miro.assignment.widget.repository.InMemoryRepository;
import com.jmbz.miro.assignment.widget.repository.WidgetRepository;
import com.jmbz.miro.assignment.widget.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service used to manage the widgets
 */
@Service
public class WidgetService  {

    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetService.class);

    private CrudRepository<Widget,String> repository;

    @Autowired
    private IndexEngine indexEngine;


    @Autowired
    WidgetRepository widgetRepository;

    @Value(value = "${widget.service.state}")
    private String state;


    @PostConstruct
    private void init() {
        this.repository=getRepository();
        this.indexEngine=new IndexEngine();
    }

    private CrudRepository<Widget, String> getRepository() throws  IllegalArgumentException{
        if(state.equalsIgnoreCase("h2")) {
            return widgetRepository;
        }
        if(state.equalsIgnoreCase("local")){
            return new InMemoryRepository();
        }

        throw new IllegalArgumentException("Invalid values for [widget.service.state]");
    }

    public List<Widget> findAll(){
        List<IndexNode> zIndexList=indexEngine.getList();
        return zIndexList.stream()
                .map(node-> repository.findById(node.getId()).get())
                .collect(Collectors.toList());
    }

    public Page<Widget> findAll(Pageable pageable){
        List<Widget> resultList=findAll();
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > resultList.size() ? resultList.size() : (start + pageable.getPageSize());
        //No more pages
        if(start>end){
            return new PageImpl<>(new ArrayList<>());
        }
        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
    }

    public Optional<Widget> findById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        indexEngine.deleteNode(id);
        repository.deleteById(id);
        LOGGER.info("Widget deleted [{}]",id);
    }

    public Widget save(Widget newWidget) {
        IndexNode insertedNode=indexEngine.insertNode(new IndexNode(newWidget.getId(),newWidget.getZIndex()));
        newWidget.setId(insertedNode.getId());
        newWidget.setZIndex(insertedNode.getZIndex());
        newWidget.setDateModif(Utils.getDate());
        LOGGER.info("Widget saved [{}]",newWidget.getId());
        return repository.save(newWidget);

    }

    public Widget update(Widget newWidget) {

        Optional<Widget> oldWidget=repository.findById(newWidget.getId());
        Widget updatedWidget=new Widget();

        if(!oldWidget.isPresent()){
            return updatedWidget;
        }

        if(newWidget.getZIndex()==null || !oldWidget.get().getZIndex().equals(newWidget.getZIndex())){
            IndexNode insertedNode=indexEngine.updateNode(new IndexNode(newWidget.getId(),newWidget.getZIndex()));
            newWidget.setZIndex(insertedNode.getZIndex());
            newWidget.setDateModif(Utils.getDate());
            updatedWidget = repository.save(newWidget);
        }
        LOGGER.info("Widget updated [{}]",updatedWidget.getId());
        return updatedWidget;
    }

    public boolean zIndexExists(Widget widget) {
        return indexEngine.zIndexExists(widget.getZIndex());
    }

    public boolean exists(String id) {
        return repository.findById(id).isPresent();
    }

    public List<Widget> getWidgetFiltered(Integer lx, Integer ly, Integer tx, Integer ty) {
        Coordinate bottomLeft=new Coordinate(lx,ly);
        Coordinate topRight=new Coordinate(tx,ty);
        return findAll().stream()
                .filter(widget -> widget.isInsideBottomLeft(bottomLeft) && widget.isInsideTopRight(topRight))
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        this.indexEngine.deleteAll();
        this.repository.deleteAll();
        LOGGER.info("All widgets deleted");
    }
}
