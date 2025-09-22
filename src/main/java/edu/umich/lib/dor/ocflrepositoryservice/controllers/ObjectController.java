package edu.umich.lib.dor.ocflrepositoryservice.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos.ObjectDto;
import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.service.Deposit;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositFactory;

@Controller
@RequestMapping(path="/object")
public class ObjectController {

    @Autowired
    private DepositFactory depositFactory;

    @PostMapping(path="/deposit")
    public @ResponseBody ObjectDto deposit(
        @RequestParam String identifier,
        @RequestParam String depositSourcePath,
        @RequestParam String message,
        @RequestParam String curatorName,
        @RequestParam String curatorEmail
    ) {
        Curator curator = new Curator(curatorName, curatorEmail);

        Path sourcePathRelativeToDeposit = Paths.get(depositSourcePath);
        Deposit deposit = depositFactory.create(
            curator, identifier, sourcePathRelativeToDeposit, message
        );
        deposit.execute();
        return new ObjectDto(identifier);
    }
}
