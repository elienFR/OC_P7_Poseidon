package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.controllers.rest.RuleNameRestController;
import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.domain.RuleName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * This class is the controller in charge of displaying views for everything that concerns
 * rule names.
 */
@Controller
@RequestMapping("/ruleName")
public class RuleNameController {

    private static final Logger LOGGER = LogManager.getLogger(RuleNameController.class);

    @Autowired
    private RuleNameRestController ruleNameRestController;

    /**
     * This method is used to display all rule names in a list.
     *
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/list")
    public String home(Model model)
    {
        LOGGER.info("Fetching /ruleName/list...");
        model.addAttribute("ruleNames", ruleNameRestController.getAll());
        return "ruleName/list";
    }

    /**
     * This method is used to add a rule name in database.
     *
     * @param ruleName is the rule name Object used to display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/add")
    public String addRuleForm(RuleName ruleName) {
        LOGGER.info("Fetching /ruleName/add...");
        return "ruleName/add";
    }

    /**
     * This method is used to validate a new rule name before adding it to database.
     *
     * @param ruleName is the ruleName Object in validation before add.
     * @param result is the BindingResult object that checks if errors are present in curve point object.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @PostMapping("/validate")
    @Transactional
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        LOGGER.info("Fetching /rating/validate...");
        LOGGER.info("Validating entries...");
        if (!result.hasErrors()) {
            LOGGER.info("Entries validated...");
            model.addAttribute("ratings", ruleNameRestController.getAll());
            ruleNameRestController.create(ruleName);
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    /**
     * This method is used to display the update page of an existing rule name before updating it to database.
     *
     * @param id is the rule name's id that is supposed to be updated.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        LOGGER.info("Fetching /ruleName/update/" + id);
        RuleName ruleName = ruleNameRestController.getById(id).orElseThrow(
          () -> new NullPointerException("No rule name found with this id (" + id + ")")
        );
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    /**
     * This method is used to validate and update an existing rule name into database.
     *
     * @param id is the rule name's id  that is supposed to be updated.
     * @param ruleName is the rule name object to update through this method.
     * @param result is the BindingResult object that checks if errors are present in rule name object.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @PostMapping("/update/{id}")
    @Transactional
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        LOGGER.info("Updating through /ruleName/update/" + id);
        LOGGER.info("Validating entries...");
        if (result.hasErrors()){
            return "ruleName/update";
        }
        LOGGER.info("Entries validated...");
        ruleNameRestController.update(ruleName);
        model.addAttribute("ruleNames", ruleNameRestController.getAll());
        return "redirect:/ruleName/list";
    }

    /**
     * This method is used to delete an existing rule name from database.
     *
     * @param id is the rule name's id that is supposed to be deleted.
     * @param model is the model that display the page correctly.
     * @return is a string path where to find the view for this controller's method.
     */
    @GetMapping("/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        LOGGER.info("Fetching /ruleName/delete/" + id);
        ruleNameRestController.delete(id);
        return "redirect:/ruleName/list";
    }
}
