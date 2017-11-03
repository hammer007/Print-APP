-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 31, 2017 at 02:01 PM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE Database `printbook`;


-- --------------------------------------------------------

--
-- Table structure for table `aging_treatment`
--

CREATE TABLE `aging_treatment` (
  `aging_id` int(11) NOT NULL,
  `temperature` double DEFAULT NULL,
  `time` time DEFAULT NULL,
  `cycles` int(11) DEFAULT NULL,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `betsurface`
--

CREATE TABLE `betsurface` (
  `Bet_ID` varchar(80) NOT NULL,
  `Material_ID` varchar(255) NOT NULL,
  `URL_Photo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `cad`
--

CREATE TABLE `cad` (
  `cad_id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `project_id` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `flow_energy`
--

CREATE TABLE `flow_energy` (
  `flow_id` int(11) NOT NULL,
  `material_id` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `temperature` double DEFAULT NULL,
  `humidity` double DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `powder_condition` varchar(10) DEFAULT NULL,
  `reused_time` int(11) DEFAULT NULL,
  `rumbling` tinyint(1) DEFAULT NULL,
  `rumbling_times` int(11) DEFAULT NULL,
  `bfe_mj` double DEFAULT NULL,
  `si` double DEFAULT NULL,
  `fri` double DEFAULT NULL,
  `se_mj_g` double DEFAULT NULL,
  `cbd_g_ml` double DEFAULT NULL,
  `cetap50_mj` double DEFAULT NULL,
  `bdtap50_g_ml` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hall_flow`
--

CREATE TABLE `hall_flow` (
  `hallflow_id` varchar(255) NOT NULL,
  `material_id` varchar(80) NOT NULL,
  `project_id` varchar(255) NOT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `humidity` double DEFAULT NULL,
  `temperature` double DEFAULT NULL,
  `value_1` double DEFAULT NULL,
  `tap_1` tinyint(1) DEFAULT NULL,
  `value_2` double DEFAULT NULL,
  `tap_2` tinyint(1) DEFAULT NULL,
  `value_3` double DEFAULT NULL,
  `tap_3` tinyint(1) DEFAULT NULL,
  `average` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hardening`
--

CREATE TABLE `hardening` (
  `hardening_id` int(11) NOT NULL,
  `temperature` double DEFAULT NULL,
  `time` time DEFAULT NULL,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `material`
--

CREATE TABLE `material` (
  `Material_ID` varchar(80) NOT NULL,
  `URL_Image` varchar(255) NOT NULL,
  `Project_ID` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `post_printing`
--

CREATE TABLE `post_printing` (
  `post_id` varchar(255) NOT NULL,
  `project_id` varchar(255) DEFAULT NULL,
  `url_photo` varchar(255) DEFAULT NULL,
  `support_removal` tinyint(1) DEFAULT NULL,
  `wedm` tinyint(1) DEFAULT NULL,
  `wedm_comment` text,
  `blasting` tinyint(1) DEFAULT NULL,
  `blasting_time` time DEFAULT NULL,
  `blasting_type` varchar(255) DEFAULT NULL,
  `blasting_comment` text,
  `stress_id` int(11) DEFAULT NULL,
  `hardening_id` int(11) DEFAULT NULL,
  `tempering_id` int(11) DEFAULT NULL,
  `solution_id` int(11) DEFAULT NULL,
  `aging_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pre_printing`
--

CREATE TABLE `pre_printing` (
  `pre_printing_id` varchar(255) NOT NULL,
  `project_id` varchar(255) DEFAULT NULL,
  `build_id` varchar(255) DEFAULT NULL,
  `no_parts` int(11) DEFAULT NULL,
  `printing_parameter` text,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `printing`
--

CREATE TABLE `printing` (
  `printing_id` int(11) NOT NULL,
  `slm_id` int(11) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `operator` varchar(255) DEFAULT NULL,
  `machine_type` varchar(255) DEFAULT NULL,
  `powder_weight_start` int(11) DEFAULT NULL,
  `powder_weight_end` int(11) DEFAULT NULL,
  `powder_waste_weight` int(11) DEFAULT NULL,
  `powder_used` int(11) DEFAULT NULL,
  `material_id` varchar(255) DEFAULT NULL,
  `build_platform_weight` varchar(255) DEFAULT NULL,
  `print_time` time DEFAULT NULL,
  `powder_condition` varchar(10) DEFAULT NULL,
  `reused_times` int(11) DEFAULT NULL,
  `number_of_layers` int(11) DEFAULT NULL,
  `dpc_factor` int(11) DEFAULT NULL,
  `exposure_time` time DEFAULT NULL,
  `comments` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `Project_ID` varchar(80) NOT NULL,
  `Name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `shear_cell`
--

CREATE TABLE `shear_cell` (
  `shear_id` int(11) NOT NULL,
  `project_id` varchar(80) DEFAULT NULL,
  `material_id` varchar(80) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `operator` varchar(255) DEFAULT NULL,
  `temperature` double DEFAULT NULL,
  `humidity` double DEFAULT NULL,
  `powder_condition` varchar(10) DEFAULT NULL,
  `reused_times` int(11) DEFAULT NULL,
  `rumbling` tinyint(1) DEFAULT NULL,
  `rumbling_time` int(11) DEFAULT NULL,
  `consolidation_pressure` double DEFAULT NULL,
  `quantity_of_powder` double DEFAULT NULL,
  `cohesion` double DEFAULT NULL,
  `uys` double DEFAULT NULL,
  `mps` double DEFAULT NULL,
  `ff` double DEFAULT NULL,
  `aif` double DEFAULT NULL,
  `bd` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `solution_treatment`
--

CREATE TABLE `solution_treatment` (
  `solution_id` int(11) NOT NULL,
  `temperature` double DEFAULT NULL,
  `time` time DEFAULT NULL,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stl`
--

CREATE TABLE `stl` (
  `STL_ID` varchar(80) NOT NULL,
  `URL` varchar(255) NOT NULL,
  `Project_ID` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `stress_relieving`
--

CREATE TABLE `stress_relieving` (
  `stress_id` int(11) NOT NULL,
  `temprature` double DEFAULT NULL,
  `time` time DEFAULT NULL,
  `shielding_gas` tinyint(1) DEFAULT NULL,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tempering`
--

CREATE TABLE `tempering` (
  `tempering_id` int(11) NOT NULL,
  `temperature` double DEFAULT NULL,
  `time` time DEFAULT NULL,
  `cycles` int(11) DEFAULT NULL,
  `comment` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aging_treatment`
--
ALTER TABLE `aging_treatment`
  ADD PRIMARY KEY (`aging_id`);

--
-- Indexes for table `betsurface`
--
ALTER TABLE `betsurface`
  ADD PRIMARY KEY (`Bet_ID`),
  ADD KEY `Material_ID` (`Material_ID`),
  ADD KEY `URL_Photo` (`URL_Photo`),
  ADD KEY `Bet_ID` (`Bet_ID`),
  ADD KEY `Material_ID_2` (`Material_ID`);

--
-- Indexes for table `cad`
--
ALTER TABLE `cad`
  ADD PRIMARY KEY (`cad_id`),
  ADD KEY `project_id` (`project_id`);

--
-- Indexes for table `flow_energy`
--
ALTER TABLE `flow_energy`
  ADD PRIMARY KEY (`flow_id`),
  ADD KEY `material_id` (`material_id`);

--
-- Indexes for table `hall_flow`
--
ALTER TABLE `hall_flow`
  ADD KEY `project_id` (`project_id`),
  ADD KEY `material_id` (`material_id`);

--
-- Indexes for table `hardening`
--
ALTER TABLE `hardening`
  ADD PRIMARY KEY (`hardening_id`);

--
-- Indexes for table `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`Material_ID`),
  ADD KEY `URL_Image` (`URL_Image`),
  ADD KEY `Project_ID` (`Project_ID`),
  ADD KEY `Material_ID` (`Material_ID`),
  ADD KEY `Project_ID_2` (`Project_ID`),
  ADD KEY `Project_ID_3` (`Project_ID`);

--
-- Indexes for table `post_printing`
--
ALTER TABLE `post_printing`
  ADD PRIMARY KEY (`post_id`),
  ADD KEY `project_id` (`project_id`),
  ADD KEY `stress_id` (`stress_id`),
  ADD KEY `hardening_id` (`hardening_id`),
  ADD KEY `aging_id` (`aging_id`),
  ADD KEY `solution_id` (`solution_id`),
  ADD KEY `tempering_id` (`tempering_id`);

--
-- Indexes for table `pre_printing`
--
ALTER TABLE `pre_printing`
  ADD PRIMARY KEY (`pre_printing_id`),
  ADD KEY `project_id` (`project_id`);

--
-- Indexes for table `printing`
--
ALTER TABLE `printing`
  ADD PRIMARY KEY (`printing_id`),
  ADD KEY `material_id` (`material_id`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`Project_ID`),
  ADD KEY `Name` (`Name`),
  ADD KEY `Project_ID` (`Project_ID`);

--
-- Indexes for table `shear_cell`
--
ALTER TABLE `shear_cell`
  ADD PRIMARY KEY (`shear_id`),
  ADD KEY `project_id` (`project_id`),
  ADD KEY `material_id` (`material_id`);

--
-- Indexes for table `solution_treatment`
--
ALTER TABLE `solution_treatment`
  ADD PRIMARY KEY (`solution_id`);

--
-- Indexes for table `stl`
--
ALTER TABLE `stl`
  ADD PRIMARY KEY (`STL_ID`),
  ADD KEY `URL` (`URL`),
  ADD KEY `Project_ID` (`Project_ID`),
  ADD KEY `Project_ID_2` (`Project_ID`);

--
-- Indexes for table `stress_relieving`
--
ALTER TABLE `stress_relieving`
  ADD PRIMARY KEY (`stress_id`);

--
-- Indexes for table `tempering`
--
ALTER TABLE `tempering`
  ADD PRIMARY KEY (`tempering_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `aging_treatment`
--
ALTER TABLE `aging_treatment`
  MODIFY `aging_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `hardening`
--
ALTER TABLE `hardening`
  MODIFY `hardening_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `printing`
--
ALTER TABLE `printing`
  MODIFY `printing_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `solution_treatment`
--
ALTER TABLE `solution_treatment`
  MODIFY `solution_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `stress_relieving`
--
ALTER TABLE `stress_relieving`
  MODIFY `stress_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tempering`
--
ALTER TABLE `tempering`
  MODIFY `tempering_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `betsurface`
--
ALTER TABLE `betsurface`
  ADD CONSTRAINT `betsurface_ibfk_1` FOREIGN KEY (`Material_ID`) REFERENCES `material` (`Material_ID`);

--
-- Constraints for table `cad`
--
ALTER TABLE `cad`
  ADD CONSTRAINT `cad_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`Project_ID`) ON UPDATE CASCADE;

--
-- Constraints for table `flow_energy`
--
ALTER TABLE `flow_energy`
  ADD CONSTRAINT `flow_energy_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `material` (`Material_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `hall_flow`
--
ALTER TABLE `hall_flow`
  ADD CONSTRAINT `hall_flow_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`Project_ID`) ON UPDATE CASCADE;

--
-- Constraints for table `material`
--
ALTER TABLE `material`
  ADD CONSTRAINT `material_ibfk_1` FOREIGN KEY (`Project_ID`) REFERENCES `project` (`Project_ID`) ON UPDATE CASCADE;

--
-- Constraints for table `printing`
--
ALTER TABLE `printing`
  ADD CONSTRAINT `printing_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `material` (`Material_ID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `shear_cell`
--
ALTER TABLE `shear_cell`
  ADD CONSTRAINT `shear_cell_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`Project_ID`) ON UPDATE CASCADE,
  ADD CONSTRAINT `shear_cell_ibfk_2` FOREIGN KEY (`material_id`) REFERENCES `material` (`Material_ID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `stl`
--
ALTER TABLE `stl`
  ADD CONSTRAINT `stl_ibfk_1` FOREIGN KEY (`Project_ID`) REFERENCES `project` (`Project_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
