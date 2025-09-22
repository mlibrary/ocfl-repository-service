package edu.umich.lib.dor.ocflrepositoryservice;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import edu.umich.lib.dor.ocflrepositoryservice.service.CommitFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositDirectory;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.OcflFilesystemRepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.PurgeFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.RepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.StageFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.UpdateFactory;

@Configuration
@ComponentScan("edu.umich.lib.dor.ocflrepositoryservice.service")
public class AppConfig {

    @Bean
    RepositoryClient respositoryClient(
        Environment environment
    ) {
        Path repoPath = Paths.get(
            environment.getRequiredProperty("repository.path")
        );

        Path repoStoragePath = repoPath.resolve("storage");
        Path repoWorkspacePath = repoPath.resolve("workspace");
        RepositoryClient repoClient = new OcflFilesystemRepositoryClient(
            repoStoragePath, repoWorkspacePath
        );
        return repoClient;
    }

    @Bean
    public DepositFactory depositFactory(
        RepositoryClient repositoryClient,
        Environment environment
    ) {
        Path depositPath = Paths.get(
            environment.getRequiredProperty("repository.deposit.path")
        );
        return new DepositFactory(
            repositoryClient,
            new DepositDirectory(depositPath)
        );
    }

    @Bean
    public UpdateFactory updateFactory(
        RepositoryClient repositoryClient,
        Environment environment
    ) {
        Path depositPath = Paths.get(
            environment.getRequiredProperty("repository.deposit.path")
        );
        return new UpdateFactory(
            repositoryClient,
            new DepositDirectory(depositPath)
        );
    }

    @Bean
    public PurgeFactory purgeFactory(RepositoryClient repositoryClient) {
        return new PurgeFactory(repositoryClient);
    }

    @Bean
    public StageFactory stageFactory(
        RepositoryClient repositoryClient,
        Environment environment
    ) {
        Path depositPath = Paths.get(
            environment.getRequiredProperty("repository.deposit.path")
        );
        return new StageFactory(
            repositoryClient,
            new DepositDirectory(depositPath)
        );
    }

    @Bean
    public CommitFactory commitFactory(RepositoryClient repositoryClient) {
        return new CommitFactory(repositoryClient);
    }
}
