# Minecraft fishing plugin

![image](https://github.com/ArtByteFilip/minecraft-fishing-plugin/assets/43612452/3b21da53-929f-4657-846e-070aca1286f3)
![image](https://github.com/ArtByteFilip/minecraft-fishing-plugin/assets/43612452/79f40b53-61f7-45eb-82b3-1662e859bd18)
![image](https://github.com/ArtByteFilip/minecraft-fishing-plugin/assets/43612452/4f1e6037-d3a9-46f4-9a6d-e8e7631ee0f5)
![image](https://github.com/ArtByteFilip/minecraft-fishing-plugin/assets/43612452/9635317c-92c7-43b7-b4c5-3edc6e973792)

```sql
CREATE TABLE `minecraft`.`betterfishing` (`id` INT(11) NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(36) NOT NULL , `rank` VARCHAR(16) NOT NULL , `level` INT(5) NOT NULL , `xp_level` INT NOT NULL , `total_fish_caught` INT NOT NULL , `week_fish_caught` INT NOT NULL , `daily_fish_caught` INT NOT NULL , `total_longest_fish` DECIMAL(8,2) NOT NULL , `week_longest_fish` DECIMAL(8,2) NOT NULL , `daily_longest_fish` DECIMAL(8,2) NOT NULL , `total_money_earned` DOUBLE NOT NULL , `updated_at` TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;
```

![image](https://github.com/ArtByteFilip/minecraft-fishing-plugin/assets/43612452/e64cf083-0fd1-4aac-8adb-fac857f2e732)
