package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.controllers.rest.RatingRestController;
import com.openclassrooms.poseidon.domain.Rating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * This class is the controller in charge of displaying views for everything that concerns
 * ratings.
 */
@Controller
@RequestMapping("/rating")
public class RatingController {

    private static final Logger LOGGER = LogManager.getLogger(RatingController.class);

    @Autowired
    private RatingRestController ratingRestController;

    /**
     * This method is used to display all ratings in a list.
     *
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/list")
    public String home(Model model)
    {
        LOGGER.info("Fetching /rating/list...");
        model.addAttribute("ratings", ratingRestController.getAll());
        return "rating/list";
    }

    /**
     * This method is used to add a rating in database.
     *
     * @param rating is the curve point Object used to display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/add")
    public String addRatingForm(Rating rating) {
        LOGGER.info("Fetching /rating/add...");
        return "rating/add";
    }

    /**
     * This method is used to validate a new rating before adding it to database.
     *
     * @param rating is the rating Object in validation before add.
     * @param result is the BindingResult object that checks if errors are present in curve point object.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @PostMapping("/validate")
    @Transactional
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        LOGGER.info("Fetching /rating/validate...");
        LOGGER.info("Validating entries...");
        if (!result.hasErrors()) {
            LOGGER.info("Entries validated...");
            model.addAttribute("ratings", ratingRestController.getAll());
            ratingRestController.create(rating);
            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    /**
     * This method is used to display the update page of an existing rating before updating it to database.
     *
     * @param id is the rating's id that is supposed to be updated.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        LOGGER.info("Fetching /rating/update/" + id);
        Rating rating = ratingRestController.getById(id).orElseThrow(
          () -> new NullPointerException("No rating found with this id (" + id + ")")
        );
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    /**
     * This method is used to validate and update an existing rating into database.
     *
     * @param id is the rating's id  that is supposed to be updated.
     * @param rating is the rating object to update through this method.
     * @param result is the BindingResult object that checks if errors are present in rating object.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @PostMapping("/update/{id}")
    @Transactional
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        LOGGER.info("Updating through /rating/update/" + id);
        LOGGER.info("Validating entries...");
        if (result.hasErrors()){
            return "rating/update";
        }
        LOGGER.info("Entries validated...");
        ratingRestController.update(rating);
        model.addAttribute("curvePoints", ratingRestController.getAll());
        return "redirect:/rating/list";
    }

    /**
     * This method is used to delete an existing rating from database.
     *
     * @param id is the rating's id that is supposed to be deleted.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        LOGGER.info("Fetching /rating/delete/" + id);
        ratingRestController.delete(id);
        return "redirect:/rating/list";
    }
}
