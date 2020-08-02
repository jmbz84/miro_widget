package com.jmbz.miro.assignment.widget.controller;


import com.jmbz.miro.assignment.widget.model.Widget;
import com.jmbz.miro.assignment.widget.services.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
class WidgetController {

    @Autowired
    private WidgetService widgetService;

    @GetMapping("/widgets")
    ResponseEntity getAllWidgets() {
        return ResponseEntity.status(HttpStatus.OK).body(widgetService.findAll());
    }

    @GetMapping(path = "/widgets/page")
    ResponseEntity getWidgetPage(@PageableDefault(value=10)Pageable pageable) {

        if(pageable.getPageSize()>500){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("page size must be lower or equal to 500");
        }

        Page<Widget> page=widgetService.findAll(pageable);
        if(page.getTotalElements()==0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(widgetService.findAll(pageable));

    }

    @PostMapping("/widget")
    ResponseEntity newWidget(@Valid @RequestBody Widget newWidget) {

        if(newWidget.getZIndex()!=null && widgetService.zIndexExists(newWidget)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("zindex already exists.");
        }
        if(newWidget.getId()!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("id must not be set.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(widgetService.save(newWidget));
    }

    @PostMapping("/widgets")
    ResponseEntity newWidgetFromList(@Valid @RequestBody List<Widget> widgetList) {

        Optional<Widget> zIndexFound=widgetList.stream().filter(  widget -> widgetService.zIndexExists(widget)).findAny();

        Optional<Widget> idFound=widgetList.stream().filter(  widget -> widget.getId()!=null).findAny();
        if(zIndexFound.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("zindex [{%s}]already exists.", zIndexFound.get().getZIndex()));
        }
        if(idFound.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("id must not be set.", zIndexFound.get().getZIndex()));
        }

        List<Widget> insertedWidgets=new ArrayList<>();
        widgetList.stream().forEach(widget -> insertedWidgets.add(widgetService.save(widget)));
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedWidgets);
    }

    @GetMapping("/widgets/filter/{lx}/{ly}/{tx}/{ty}")
    ResponseEntity getWidgetFiltered(@PathVariable Integer lx, @PathVariable Integer ly,
                                     @PathVariable Integer tx, @PathVariable Integer ty) {
        return ResponseEntity.status(HttpStatus.OK).body(widgetService.getWidgetFiltered(lx,ly,tx,ty));
    }

    @GetMapping("/widget/{id}")
    ResponseEntity getWidget(@PathVariable String id) {

        if(id != null && !widgetService.exists(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id not found.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                        .body(widgetService.findById(id).get());

    }

    @DeleteMapping("/widget/{id}")
    ResponseEntity deleteWidget(@PathVariable String id) {
        widgetService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/widgets")
    ResponseEntity deleteAll() {
        widgetService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(("/widget/update/{id}"))
    ResponseEntity updateWidget(@RequestBody Widget newWidget, @PathVariable String id){

        if(newWidget.getZIndex()!=null && widgetService.zIndexExists(newWidget)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("zindex already exists.");
        }
        if(id != null && !widgetService.exists(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id not found.");
        }
        newWidget.setId(id);
        Widget updatedWidget=widgetService.update(newWidget);
        if (updatedWidget!=null){
            return ResponseEntity.status(HttpStatus.OK).body(updatedWidget);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");

    }

}