-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Oct 30, 2017 at 09:32 AM
-- Server version: 5.7.19
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- create Database: `printbook`
--

-- --------------------------------------------------------

--
-- Table structure for table `betsurface`
--

DROP TABLE IF EXISTS `betsurface`;
CREATE TABLE IF NOT EXISTS `betsurface` (
  `Bet_ID` varchar(80) NOT NULL,
  `Material_ID` varchar(255) NOT NULL,
  `URL_Photo` varchar(255) NOT NULL,
  PRIMARY KEY (`Bet_ID`),
  KEY `Material_ID` (`Material_ID`),
  KEY `URL_Photo` (`URL_Photo`),
  KEY `Bet_ID` (`Bet_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
CREATE TABLE IF NOT EXISTS `material` (
  `Material_ID` varchar(80) NOT NULL,
  `URL_Image` varchar(255) NOT NULL,
  `Project_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`Material_ID`),
  KEY `URL_Image` (`URL_Image`),
  KEY `Project_ID` (`Project_ID`),
  KEY `Material_ID` (`Material_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project` (
  `Project_ID` varchar(80) NOT NULL,
  `Name` varchar(255) NOT NULL,
  PRIMARY KEY (`Project_ID`),
  KEY `Name` (`Name`),
  KEY `Project_ID` (`Project_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stl`
--

DROP TABLE IF EXISTS `stl`;
CREATE TABLE IF NOT EXISTS `stl` (
  `STL_ID` varchar(80) NOT NULL,
  `URL` varchar(255) NOT NULL,
  `Project_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`STL_ID`),
  KEY `URL` (`URL`),
  KEY `Project_ID` (`Project_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
