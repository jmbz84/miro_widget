package com.jmbz.miro.assignment.widget.repository;


import com.jmbz.miro.assignment.widget.model.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In Memory widget repository
 */
@Component
public class InMemoryRepository implements CrudRepository<Widget,String> {
    Map<String, Widget> repository=new HashMap<>();

    @Override
    public Widget save(Widget widget) {
        repository.put(widget.getId(),widget);
        return widget;
    }

    @Override
    public <S extends Widget> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Widget> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public Iterable<Widget> findAll() {
        return repository.values();
    }

    @Override
    public Iterable<Widget> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return repository.values().size();
    }

    @Override
    public void deleteById(String id) {
        repository.remove(id);
    }

    @Override
    public void delete(Widget widget) {
    }

    @Override
    public void deleteAll(Iterable<? extends Widget> iterable) {
    }

    @Override
    public void deleteAll() {
        this.repository.clear();
    }
}
