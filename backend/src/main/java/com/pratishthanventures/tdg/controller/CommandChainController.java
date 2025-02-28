package com.pratishthanventures.tdg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.PatternType;
import com.pratishthanventures.tdg.model.CommandChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping("/command-chain")
public class CommandChainController {

    @GetMapping
    public String showCommandChainForm(Model model) {
        model.addAttribute("commandChain", new CommandChain());
        model.addAttribute("patternTypes", PatternType.values());
        return "command-chain-form";
    }

    @PostMapping
    public String saveCommandChain(@RequestBody CommandChain commandChain) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/resources/CommandChain.json"), commandChain);
        } catch (IOException e) {
            // Handle exception
        }
        return "redirect:/command-chain";
    }
}