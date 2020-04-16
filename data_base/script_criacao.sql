-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pd_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pd_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pd_db` DEFAULT CHARACTER SET utf8 ;
USE `pd_db` ;

-- -----------------------------------------------------
-- Table `pd_db`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_db`.`User` (
  `Username` VARCHAR(45) NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_db`.`Song`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_db`.`Song` (
  `Name` VARCHAR(45) NOT NULL,
  `Author` VARCHAR(45) NOT NULL,
  `Path` VARCHAR(128) NOT NULL,
  `Duration` FLOAT NOT NULL,
  `Year` INT NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `Album` VARCHAR(45) NOT NULL,
  `User_Username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Name`),
  INDEX `fk_Song_User1_idx` (`User_Username` ASC) VISIBLE,
  CONSTRAINT `fk_Song_User1`
    FOREIGN KEY (`User_Username`)
    REFERENCES `pd_db`.`User` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_db`.`Playlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_db`.`Playlist` (
  `Name` VARCHAR(45) NOT NULL,
  `User_Username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Name`),
  INDEX `fk_Playlist_User1_idx` (`User_Username` ASC) VISIBLE,
  CONSTRAINT `fk_Playlist_User1`
    FOREIGN KEY (`User_Username`)
    REFERENCES `pd_db`.`User` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_db`.`Playlist_has_Song`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_db`.`Playlist_has_Song` (
  `Playlist_Name` VARCHAR(45) NOT NULL,
  `Song_Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Playlist_Name`, `Song_Name`),
  INDEX `fk_Playlist_has_Song_Song1_idx` (`Song_Name` ASC) VISIBLE,
  INDEX `fk_Playlist_has_Song_Playlist1_idx` (`Playlist_Name` ASC) VISIBLE,
  CONSTRAINT `fk_Playlist_has_Song_Playlist1`
    FOREIGN KEY (`Playlist_Name`)
    REFERENCES `pd_db`.`Playlist` (`Name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Playlist_has_Song_Song1`
    FOREIGN KEY (`Song_Name`)
    REFERENCES `pd_db`.`Song` (`Name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
