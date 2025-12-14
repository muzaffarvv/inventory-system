package uz.pdp.inventorymanagementsystem.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import uz.pdp.inventorymanagementsystem.base.BaseRepoImpl

@Configuration
@EnableJpaRepositories(
    basePackages = ["uz.pdp.inventorymanagementsystem.base",
        "uz.pdp.inventorymanagementsystem.repo"],
    repositoryBaseClass = BaseRepoImpl::class
)
class JpaConfig
