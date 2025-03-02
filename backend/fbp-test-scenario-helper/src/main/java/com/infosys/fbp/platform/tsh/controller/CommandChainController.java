package com.infosys.fbp.platform.tsh.controller;

import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.service.ActionChainConverter;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;

@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/command-chain")
public class CommandChainController {

    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateWorkbook(@RequestBody CommandChain commandChain){
        try{
            log.info("About to process {}", commandChain);
            ActionChainConverter actionChainConverter = new ActionChainConverter();
            TDGWorkbook workbook = actionChainConverter.process(commandChain);
            if(workbook == null){
                return ResponseEntity.badRequest().body("Invalid Command Chain".getBytes());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.getWorkbook().write(baos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(commandChain.getExcelFileName()).build());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }


    }
}