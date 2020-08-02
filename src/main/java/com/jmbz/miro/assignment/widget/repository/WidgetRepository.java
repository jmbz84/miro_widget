package com.jmbz.miro.assignment.widget.repository;


import com.jmbz.miro.assignment.widget.model.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WidgetRepository extends JpaRepository<Widget, String> {
}