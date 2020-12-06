package com.example.tacocloud.controller;

import com.example.tacocloud.dao.IngredientRepository;
import com.example.tacocloud.dao.TacoRepository;
import com.example.tacocloud.model.Ingredient;
import com.example.tacocloud.model.Order;
import com.example.tacocloud.model.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;

    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        Order order = new Order();
        order.setTacos(new ArrayList<>());
        return order;
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        Taco taco = new Taco();
        order().getTacos().add(taco);
        return taco;
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
    }

    @PostMapping
    public String processDesign(@Valid Taco taco, Errors errors,
                                @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }

        Taco saved = tacoRepository.save(taco);
        order.getTacos().add(saved);

        log.info("Processing design: " + taco);
        return "redirect:/orders/current";
    }
}
