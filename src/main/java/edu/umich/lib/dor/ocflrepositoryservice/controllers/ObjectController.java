package edu.umich.lib.dor.ocflrepositoryservice.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos.ObjectDto;
import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.service.Commit;
import edu.umich.lib.dor.ocflrepositoryservice.service.CommitFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.Deposit;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.Purge;
import edu.umich.lib.dor.ocflrepositoryservice.service.PurgeFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.Stage;
import edu.umich.lib.dor.ocflrepositoryservice.service.StageFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.Update;
import edu.umich.lib.dor.ocflrepositoryservice.service.UpdateFactory;

@Controller
@RequestMapping(path="/object")
public class ObjectController {

    @Autowired
    private DepositFactory depositFactory;

    @Autowired
    private UpdateFactory updateFactory;

    @Autowired
    private PurgeFactory purgeFactory;

    @Autowired
    private StageFactory stageFactory;

    @Autowired
    private CommitFactory commitFactory;

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
            identifier, sourcePathRelativeToDeposit, curator, message
        );
        deposit.execute();
        return new ObjectDto(identifier);
    }

    @PostMapping(path="/update")
    public @ResponseBody ObjectDto update(
        @RequestParam String identifier,
        @RequestParam String depositSourcePath,
        @RequestParam String message,
        @RequestParam String curatorName,
        @RequestParam String curatorEmail
    ) {
        Curator curator = new Curator(curatorName, curatorEmail);

        Path sourcePathRelativeToDeposit = Paths.get(depositSourcePath);
        Update update = updateFactory.create(
            identifier, sourcePathRelativeToDeposit, curator, message
        );
        update.execute();
        return new ObjectDto(identifier);
    }

    @DeleteMapping(path="/purge")
    public @ResponseBody String purge(
        @RequestParam String identifier
    ) {
        Purge purge = purgeFactory.create(identifier);
        purge.execute();
        return "Purged";
    }

    @PostMapping(path="/stage")
    public @ResponseBody ObjectDto stage(
        @RequestParam String identifier,
        @RequestParam String depositSourcePath,
        @RequestParam String message,
        @RequestParam String curatorName,
        @RequestParam String curatorEmail
    ) {
        Curator curator = new Curator(curatorName, curatorEmail);

        Path sourcePathRelativeToDeposit = Paths.get(depositSourcePath);
        Stage stage = stageFactory.create(
            curator, identifier, sourcePathRelativeToDeposit, message
        );
        stage.execute();
        return new ObjectDto(identifier);
    }

    @PostMapping(path="/commit")
    public @ResponseBody ObjectDto commit(
        @RequestParam String identifier,
        @RequestParam String message,
        @RequestParam String curatorName,
        @RequestParam String curatorEmail
    ) {
        Curator curator = new Curator(curatorName, curatorEmail);

        Commit commit = commitFactory.create(identifier, curator, message);
        commit.execute();
        return new ObjectDto(identifier);
    }
}
