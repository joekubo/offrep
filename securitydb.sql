-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 20, 2018 at 10:20 PM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `securitydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `backup_table`
--

CREATE TABLE `backup_table` (
  `id` int(11) NOT NULL,
  `final_date` date NOT NULL,
  `today_date` date NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `backup_table`
--

INSERT INTO `backup_table` (`id`, `final_date`, `today_date`, `s`) VALUES
(1, '0000-00-00', '0000-00-00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `Clientstable`
--

CREATE TABLE `Clientstable` (
  `clientid` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pin` varchar(255) NOT NULL,
  `balance` double NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Clientstable`
--

INSERT INTO `Clientstable` (`clientid`, `name`, `address`, `city`, `phone_no`, `email`, `pin`, `balance`, `s`) VALUES
(1, 'Our Company', '-', '-', '-', '-', '-', 0, 1),
(6, 'BAFAGIH SUPERMARKET', '0345345-80300', 'VOI', '0723423424', 'bafgahi@gmail.com', 'P9089988G', 273800, 1),
(7, 'AILAZ SUPERMARKET', '08088-800300', 'VOI', '072342340', 'alilaz@gmail.com', 'P09234823K', 9000, 0),
(8, 'RIKENS SUPERMARKET', '8988-80100', 'MOMBASA', '072234213', 'ricken@gmail.com', 'T0345344L', 0, 1),
(9, 'NAKUMATT NYALI', '0834534-80100', 'MOMBASA', '0723423423', 'naya@gmail.com', 'P0767768765G', 1000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `clients_paytable`
--

CREATE TABLE `clients_paytable` (
  `payment_id` varchar(255) NOT NULL,
  `payment_date` varchar(255) NOT NULL,
  `client_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `s` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clients_paytable`
--

INSERT INTO `clients_paytable` (`payment_id`, `payment_date`, `client_id`, `amount`, `s`, `user_id`) VALUES
('PYMT-180413090832', '2018-04-13', 9, 8000, 1, 1),
('PYMT-180413094441', '2018-04-13', 6, 9000, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `companytable`
--

CREATE TABLE `companytable` (
  `id` int(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `fax` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pin` varchar(255) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `companytable`
--

INSERT INTO `companytable` (`id`, `company_name`, `location`, `address`, `city`, `phone_no`, `fax`, `email`, `pin`, `s`) VALUES
(1, 'Urban Force Group Limited', 'MOI AVENUE, KARIMI HSE', '123- 80100', 'MOMBASA', '0', '-', 'info@urbanforcegroup.com', '-', 1);

-- --------------------------------------------------------

--
-- Table structure for table `expensestable`
--

CREATE TABLE `expensestable` (
  `expense_id` varchar(255) NOT NULL,
  `expense_date` date NOT NULL,
  `expense_name` varchar(255) NOT NULL,
  `mop` varchar(255) NOT NULL,
  `transactionNo` varchar(255) NOT NULL,
  `amount` double NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `expensestable`
--

INSERT INTO `expensestable` (`expense_id`, `expense_date`, `expense_name`, `mop`, `transactionNo`, `amount`, `s`) VALUES
('EXP-180412182945', '2018-04-12', '+\"\'; DELETE FROM USERSTABLE;', 'Cash', '-', 9000, 0),
('EXP-180413091042', '2018-04-13', 'ELECTRICITY', 'Cash', '-', 1300, 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoiceinfo`
--

CREATE TABLE `invoiceinfo` (
  `id` int(11) NOT NULL,
  `invoice_no` varchar(255) NOT NULL,
  `service_id` int(11) NOT NULL,
  `price` double NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invoiceinfo`
--

INSERT INTO `invoiceinfo` (`id`, `invoice_no`, `service_id`, `price`, `s`) VALUES
(99, 'INV-180412170635', 8, 9000, 0),
(100, 'INV-180412174624', 9, 89000, 0),
(101, 'INV-180412174702', 8, 8900, 0),
(102, 'INV-180412174702', 9, 56000, 0),
(103, 'INV-180412175630', 8, 9000, 1),
(104, 'INV-180412175630', 9, 90000, 1),
(105, 'INV-180412175630', 10, 78900, 1),
(106, 'INV-180412180209', 9, 80000, 0),
(107, 'INV-180413090454', 8, 6000, 1),
(108, 'INV-180413090454', 9, 9000, 1),
(109, 'INV-180413090945', 9, 9000, 1),
(110, 'INV-180413125337', 8, 9000, 1),
(111, 'INV-180414210431', 8, 10000, 1),
(112, 'INV-180414210431', 9, 50000, 1),
(113, 'INV-180414210431', 10, 8900, 1),
(114, 'INV-180414210431', 11, 12000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoicetable`
--

CREATE TABLE `invoicetable` (
  `invoice_no` varchar(255) NOT NULL,
  `client_id` int(11) NOT NULL,
  `invoice_date` varchar(255) NOT NULL,
  `total` double NOT NULL,
  `user_id` int(11) NOT NULL,
  `s` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invoicetable`
--

INSERT INTO `invoicetable` (`invoice_no`, `client_id`, `invoice_date`, `total`, `user_id`, `s`) VALUES
('INV-180412170635', 6, '2018-04-12', 9000, 1, '0'),
('INV-180412174624', 6, '2018-04-12', 89000, 1, '0'),
('INV-180412174702', 8, '2018-04-12', 64900, 1, '0'),
('INV-180412175630', 6, '2018-04-12', 177900, 1, '1'),
('INV-180412180209', 8, '2018-04-12', 80000, 1, '0'),
('INV-180413090454', 6, '2018-04-13', 15000, 1, '1'),
('INV-180413090945', 9, '2018-04-13', 9000, 1, '1'),
('INV-180413125337', 6, '2018-04-13', 9000, 1, '1'),
('INV-180414210431', 6, '2018-04-14', 80900, 1, '1');

-- --------------------------------------------------------

--
-- Table structure for table `logtable`
--

CREATE TABLE `logtable` (
  `id` int(11) NOT NULL,
  `date` varchar(255) NOT NULL,
  `operation` varchar(255) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `logtable`
--

INSERT INTO `logtable` (`id`, `date`, `operation`, `s`) VALUES
(1004, '2018-04-12', '23281962-Joseph Tolah Updated EMILY KUBO ON 2018-04-12 AT 11:52:22', 1),
(1005, '2018-04-12', '23281962-Joseph Tolah Updated User EMILY KUBO ON 2018-04-12 AT 11:52:48', 1),
(1006, '2018-04-12', '23281962-Joseph Tolah Updated EMILY KUBO ON 2018-04-12 AT 11:52:49', 1),
(1007, '2018-04-12', '23281962-Joseph Tolah Updated User EMILY KUBO ON 2018-04-12 AT 11:52:49', 1),
(1008, '2018-04-12', '23281962-Joseph Tolah Updated EMILY KUBO ON 2018-04-12 AT 11:52:49', 1),
(1009, '2018-04-12', '23281962-Joseph Tolah Updated User EMILY KUBO ON 2018-04-12 AT 11:52:49', 1),
(1010, '2018-04-12', '23281962-Joseph Tolah Updated EMILY KUBO ON 2018-04-12 AT 11:52:49', 1),
(1011, '2018-04-12', '23281962-Joseph Tolah Deleted EMILY KUBO details ON 2018-04-12 AT 11:53:09', 1),
(1012, '2018-04-12', '23281962-Joseph Tolah Updated Service CCTV INSTALLATION AND MAINTENANCE ON 2018-04-12 AT 11:55:00', 1),
(1013, '2018-04-12', '23281962-Joseph Tolah is logged in at 11:56:59', 1),
(1014, '2018-04-12', '23281962-Joseph Tolah Updated Service SECURITY GUARDS ON 2018-04-12 AT 11:57:11', 1),
(1015, '2018-04-12', '23281962-Joseph Tolah is logged in at 11:59:29', 1),
(1016, '2018-04-12', '23281962-Joseph Tolah Updated Service SECURITY GUARD HIRE ON 2018-04-12 AT 11:59:48', 1),
(1017, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:01:08', 1),
(1018, '2018-04-12', '23281962-Joseph Tolah Updated Service SECURITY GUARD HIRE ON 2018-04-12 AT 12:01:14', 1),
(1019, '2018-04-12', '23281962-Joseph Tolah Updated Client AILAZ SUPERMARKET ON 2018-04-12 AT 12:02:41', 1),
(1020, '2018-04-12', '23281962-Joseph Tolah Updated Client AILAZ SUPERMARKET ON 2018-04-12 AT 12:02:48', 1),
(1021, '2018-04-12', '23281962-Joseph Tolah Updated Client AILAZ SUPERMARKET ON 2018-04-12 AT 12:02:53', 1),
(1022, '2018-04-12', '23281962-Joseph Tolah Updated Client AILAZ SUPERMARKET ON 2018-04-12 AT 12:03:06', 1),
(1023, '2018-04-12', '23281962-Joseph Tolah Updated Invoice # INV-1804121204381 ON 2018-04-12 AT 12:04:56', 1),
(1024, '2018-04-12', '23281962-Joseph Tolah Updated Invoice # INV-1804121204381 ON 2018-04-12 AT 12:05:42', 1),
(1025, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:07:25', 1),
(1026, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:07:33', 1),
(1027, '2018-04-12', '23281962-Joseph Tolah Updated Invoice # INV-1804121207411 ON 2018-04-12 AT 12:07:43', 1),
(1028, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121204381 details ON 2018-04-12 AT 12:08:33', 1),
(1029, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:08:36', 1),
(1030, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:08:46', 1),
(1031, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:08:52', 1),
(1032, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:10:36', 1),
(1033, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121210461 details ON 2018-04-12 AT 12:10:55', 1),
(1034, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:10:59', 1),
(1035, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:11:11', 1),
(1036, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:13:28', 1),
(1037, '2018-04-12', 'null-null has logged out at 12:14:53', 1),
(1038, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:17:17', 1),
(1039, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121212531 details ON 2018-04-12 AT 12:17:27', 1),
(1040, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:17:29', 1),
(1041, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:17:37', 1),
(1042, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121217411 details ON 2018-04-12 AT 12:17:52', 1),
(1043, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121217411 details ON 2018-04-12 AT 12:18:10', 1),
(1044, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121217411 details ON 2018-04-12 AT 12:18:13', 1),
(1045, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:30:01', 1),
(1046, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121230081 details ON 2018-04-12 AT 12:30:19', 1),
(1047, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:34:16', 1),
(1048, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:35:08', 1),
(1049, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121235121 details ON 2018-04-12 AT 12:35:25', 1),
(1050, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:35:29', 1),
(1051, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:36:58', 1),
(1052, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121237031 details ON 2018-04-12 AT 12:37:13', 1),
(1053, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:37:16', 1),
(1054, '2018-04-12', '23281962-Joseph Tolah is logged in at 12:39:16', 1),
(1055, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:31', 1),
(1056, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:34', 1),
(1057, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:35', 1),
(1058, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:35', 1),
(1059, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:35', 1),
(1060, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:35', 1),
(1061, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:36', 1),
(1062, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:36', 1),
(1063, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:36', 1),
(1064, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:36', 1),
(1065, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:36', 1),
(1066, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:37', 1),
(1067, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:37', 1),
(1068, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:37', 1),
(1069, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121239281 details ON 2018-04-12 AT 12:39:37', 1),
(1070, '2018-04-12', '23281962-Joseph Tolah has logged out at 12:39:39', 1),
(1071, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:09:03', 1),
(1072, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:09:40', 1),
(1073, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:12:08', 1),
(1074, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:12:35', 1),
(1075, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:13:12', 1),
(1076, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:13:37', 1),
(1077, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:14:33', 1),
(1078, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121314541 details ON 2018-04-12 AT 13:14:58', 1),
(1079, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:15:02', 1),
(1080, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:15:42', 1),
(1081, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:15:50', 1),
(1082, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:16:31', 1),
(1083, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:16:37', 1),
(1084, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:17:26', 1),
(1085, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121313161 details ON 2018-04-12 AT 13:17:32', 1),
(1086, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121314471 details ON 2018-04-12 AT 13:17:34', 1),
(1087, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:17:38', 1),
(1088, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:17:47', 1),
(1089, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121317501 details ON 2018-04-12 AT 13:17:58', 1),
(1090, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121317501 details ON 2018-04-12 AT 13:18:02', 1),
(1091, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:18:07', 1),
(1092, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:21:33', 1),
(1093, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:26:33', 1),
(1094, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121321371 details ON 2018-04-12 AT 13:26:43', 1),
(1095, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121321371 details ON 2018-04-12 AT 13:27:28', 1),
(1096, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121317501 details ON 2018-04-12 AT 13:27:33', 1),
(1097, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121314471 details ON 2018-04-12 AT 13:27:39', 1),
(1098, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121313161 details ON 2018-04-12 AT 13:27:41', 1),
(1099, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121327461 details ON 2018-04-12 AT 13:28:04', 1),
(1100, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:28:17', 1),
(1101, '2018-04-12', 'null-null has logged out at 13:33:58', 1),
(1102, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:34:36', 1),
(1103, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121334401 details ON 2018-04-12 AT 13:34:48', 1),
(1104, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:35:00', 1),
(1105, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:35:14', 1),
(1106, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121335171 details ON 2018-04-12 AT 13:35:26', 1),
(1107, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:36:41', 1),
(1108, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121336471 details ON 2018-04-12 AT 13:36:58', 1),
(1109, '2018-04-12', '23281962-Joseph Tolah is logged in at 13:42:07', 1),
(1110, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121342121 details ON 2018-04-12 AT 13:42:21', 1),
(1111, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121342121 details ON 2018-04-12 AT 13:42:25', 1),
(1112, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:42:32', 1),
(1113, '2018-04-12', '23281962-Joseph Tolah has logged out at 13:59:08', 1),
(1114, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:02:46', 1),
(1115, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121402491 details ON 2018-04-12 AT 14:02:57', 1),
(1116, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:04:11', 1),
(1117, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121404141 details ON 2018-04-12 AT 14:04:23', 1),
(1118, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121404141 details ON 2018-04-12 AT 14:04:44', 1),
(1119, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:05:15', 1),
(1120, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:05:23', 1),
(1121, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:05:47', 1),
(1122, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:07:44', 1),
(1123, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:09:20', 1),
(1124, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121409231 details ON 2018-04-12 AT 14:09:34', 1),
(1125, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:09:49', 1),
(1126, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:09:59', 1),
(1127, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121410061 details ON 2018-04-12 AT 14:10:16', 1),
(1128, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121410061 details ON 2018-04-12 AT 14:10:32', 1),
(1129, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:10:42', 1),
(1130, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:11:35', 1),
(1131, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121411391 details ON 2018-04-12 AT 14:11:50', 1),
(1132, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:11:53', 1),
(1133, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:12:31', 1),
(1134, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121412341 details ON 2018-04-12 AT 14:12:43', 1),
(1135, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121412341 details ON 2018-04-12 AT 14:12:47', 1),
(1136, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:14:34', 1),
(1137, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121414421 details ON 2018-04-12 AT 14:14:50', 1),
(1138, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:15:16', 1),
(1139, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:16:35', 1),
(1140, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121416401 details ON 2018-04-12 AT 14:17:33', 1),
(1141, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:17:49', 1),
(1142, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:18:33', 1),
(1143, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121418341 details ON 2018-04-12 AT 14:18:46', 1),
(1144, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:18:54', 1),
(1145, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:24:26', 1),
(1146, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121424281 details ON 2018-04-12 AT 14:24:39', 1),
(1147, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:24:51', 1),
(1148, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:29:26', 1),
(1149, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121429281 details ON 2018-04-12 AT 14:29:53', 1),
(1150, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:30:24', 1),
(1151, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:30:42', 1),
(1152, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121430441 details ON 2018-04-12 AT 14:31:00', 1),
(1153, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:31:34', 1),
(1154, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:33:46', 1),
(1155, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121433481 details ON 2018-04-12 AT 14:34:02', 1),
(1156, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:34:21', 1),
(1157, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:34:46', 1),
(1158, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121434471 details ON 2018-04-12 AT 14:34:59', 1),
(1159, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:35:07', 1),
(1160, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:44:00', 1),
(1161, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121444011 details ON 2018-04-12 AT 14:44:13', 1),
(1162, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121444011 details ON 2018-04-12 AT 14:44:34', 1),
(1163, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:44:56', 1),
(1164, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:47:51', 1),
(1165, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121448051 details ON 2018-04-12 AT 14:48:21', 1),
(1166, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121447531 details ON 2018-04-12 AT 14:48:24', 1),
(1167, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:48:38', 1),
(1168, '2018-04-12', '23281962-Joseph Tolah is logged in at 14:59:00', 1),
(1169, '2018-04-12', '23281962-Joseph Tolah has logged out at 14:59:31', 1),
(1170, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:00:09', 1),
(1171, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121459021 details ON 2018-04-12 AT 15:00:14', 1),
(1172, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121459021 details ON 2018-04-12 AT 15:00:26', 1),
(1173, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121459021 details ON 2018-04-12 AT 15:00:29', 1),
(1174, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:00:32', 1),
(1175, '2018-04-12', 'null-null has logged out at 15:01:16', 1),
(1176, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:01:46', 1),
(1177, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121501481 details ON 2018-04-12 AT 15:02:02', 1),
(1178, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121501481 details ON 2018-04-12 AT 15:02:05', 1),
(1179, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121501481 details ON 2018-04-12 AT 15:02:10', 1),
(1180, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:02:41', 1),
(1181, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:02:52', 1),
(1182, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:03:31', 1),
(1183, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:04:07', 1),
(1184, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121502531 details ON 2018-04-12 AT 15:04:12', 1),
(1185, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121502531 details ON 2018-04-12 AT 15:04:24', 1),
(1186, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-1804121502531 details ON 2018-04-12 AT 15:04:25', 1),
(1187, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:04:38', 1),
(1188, '2018-04-12', 'null-null has logged out at 15:06:05', 1),
(1189, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:06:25', 1),
(1190, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:06:47', 1),
(1191, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:07:33', 1),
(1192, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:08:28', 1),
(1193, '2018-04-12', '23281962-Joseph Tolah is logged in at 15:09:20', 1),
(1194, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412150921 details ON 2018-04-12 AT 15:09:38', 1),
(1195, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412150735 details ON 2018-04-12 AT 15:09:41', 1),
(1196, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412150921 details ON 2018-04-12 AT 15:10:11', 1),
(1197, '2018-04-12', '23281962-Joseph Tolah has logged out at 15:10:42', 1),
(1198, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:11:55', 1),
(1199, '2018-04-12', '23281962-Joseph Tolah Updated Client AILAZ SUPERMARKET ON 2018-04-12 AT 16:12:30', 1),
(1200, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:12:42', 1),
(1201, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:13:36', 1),
(1202, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:15:46', 1),
(1203, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:16:10', 1),
(1204, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:19:57', 1),
(1205, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:20:09', 1),
(1206, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:20:22', 1),
(1207, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:21:28', 1),
(1208, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412162244 details ON 2018-04-12 AT 16:31:58', 1),
(1209, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:37:24', 1),
(1210, '2018-04-12', 'null-null has logged out at 16:41:40', 1),
(1211, '2018-04-12', '23281962-Joseph Tolah is logged in at 16:55:55', 1),
(1212, '2018-04-12', '23281962-Joseph Tolah has logged out at 16:58:48', 1),
(1213, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:01:25', 1),
(1214, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:01:28', 1),
(1215, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:02:08', 1),
(1216, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:02:17', 1),
(1217, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:02:47', 1),
(1218, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:03:03', 1),
(1219, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:03:41', 1),
(1220, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:04:17', 1),
(1221, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:04:28', 1),
(1222, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:05:27', 1),
(1223, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:05:42', 1),
(1224, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:05:54', 1),
(1225, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412170635 details ON 2018-04-12 AT 17:07:52', 1),
(1226, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:12:52', 1),
(1227, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:13:09', 1),
(1228, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:14:22', 1),
(1229, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:24:35', 1),
(1230, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:24:45', 1),
(1231, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:26:18', 1),
(1232, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:27:48', 1),
(1233, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:46:18', 1),
(1234, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412174624 details ON 2018-04-12 AT 17:46:38', 1),
(1235, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412174702 details ON 2018-04-12 AT 17:49:42', 1),
(1236, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:50:31', 1),
(1237, '2018-04-12', '23281962-Joseph Tolah is logged in at 17:56:02', 1),
(1238, '2018-04-12', '23281962-Joseph Tolah has logged out at 17:58:17', 1),
(1239, '2018-04-12', '23281962-Joseph Tolah is logged in at 18:00:10', 1),
(1240, '2018-04-12', '23281962-Joseph Tolah has logged out at 18:01:06', 1),
(1241, '2018-04-12', '23281962-Joseph Tolah is logged in at 18:01:34', 1),
(1242, '2018-04-12', '23281962-Joseph Tolah is logged in at 18:06:15', 1),
(1243, '2018-04-12', '23281962-Joseph Tolah is logged in at 19:05:11', 1),
(1244, '2018-04-12', '23281962-Joseph Tolah has logged out at 19:27:24', 1),
(1245, '2018-04-12', '23281962-Joseph Tolah has logged out at 19:28:39', 1),
(1246, '2018-04-12', '23281962-Joseph Tolah is logged in at 21:12:31', 1),
(1247, '2018-04-12', '23281962-Joseph Tolah Deleted Invoice # INV-180412180209 details ON 2018-04-12 AT 21:12:52', 1),
(1248, '2018-04-12', '23281962-Joseph Tolah has logged out at 21:12:55', 1),
(1249, '2018-04-12', '23281962-Joseph Tolah is logged in at 21:14:26', 1),
(1250, '2018-04-12', '23281962-Joseph Tolah has logged out at 21:26:45', 1),
(1251, '2018-04-12', '23281962-Joseph Tolah is logged in at 21:47:08', 1),
(1252, '2018-04-12', '23281962-Joseph Tolah has logged out at 21:48:33', 1),
(1253, '2018-04-12', '23281962-Joseph Tolah is logged in at 22:47:20', 1),
(1254, '2018-04-12', '23281962-Joseph Tolah has logged out at 22:52:11', 1),
(1255, '2018-04-13', '23281962-Joseph Tolah is logged in at 08:26:19', 1),
(1256, '2018-04-13', '23281962-Joseph Tolah has logged out at 08:31:05', 1),
(1257, '2018-04-13', '23281962-Joseph Tolah is logged in at 08:32:41', 1),
(1258, '2018-04-13', '23281962-Joseph Tolah is logged in at 08:42:57', 1),
(1259, '2018-04-13', '23281962-Joseph Tolah has logged out at 08:43:31', 1),
(1260, '2018-04-13', '23281962-Joseph Tolah is logged in at 08:58:52', 1),
(1261, '2018-04-13', '23281962-Joseph Tolah Updated Client NAKUMATT NYALI ON 2018-04-13 AT 09:18:18', 1),
(1262, '2018-04-13', '23281962-Joseph Tolah is logged in at 09:41:32', 1),
(1263, '2018-04-13', '23281962-Joseph Tolah is logged in at 09:42:25', 1),
(1264, '2018-04-13', '23281962-Joseph Tolah has logged out at 09:42:40', 1),
(1265, '2018-04-13', '23281962-Joseph Tolah is logged in at 09:44:38', 1),
(1266, '2018-04-13', '23281962-Joseph Tolah has logged out at 09:45:30', 1),
(1267, '2018-04-13', '23281962-Joseph Tolah is logged in at 09:46:36', 1),
(1268, '2018-04-13', '23281962-Joseph Tolah has logged out at 09:58:10', 1),
(1269, '2018-04-13', '23281962-Joseph Tolah is logged in at 12:53:17', 1),
(1270, '2018-04-13', '23281962-Joseph Tolah Updated Client NAKUMATT NYALI ON 2018-04-13 AT 12:55:28', 1),
(1271, '2018-04-13', '23281962-Joseph Tolah Updated Service SECURITY GUARD HIRE ON 2018-04-13 AT 12:56:28', 1),
(1272, '2018-04-13', '23281962-Joseph Tolah Updated Service SECURITY GUARD ON 2018-04-13 AT 12:57:24', 1),
(1273, '2018-04-13', '23281962-Joseph Tolah is logged in at 13:01:31', 1),
(1274, '2018-04-13', '23281962-Joseph Tolah Updated Client RIKENS SUPERMARKET\' ON 2018-04-13 AT 13:05:10', 1),
(1275, '2018-04-13', '23281962-Joseph Tolah has logged out at 13:38:14', 1),
(1276, '2018-04-13', '23281962-Joseph Tolah has logged out at 22:22:03', 1),
(1277, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:42:28', 1),
(1278, '2018-04-14', '23281962-Joseph Tolah has logged out at 00:42:51', 1),
(1279, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:44:00', 1),
(1280, '2018-04-14', '23281962-Joseph Tolah has logged out at 00:44:50', 1),
(1281, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:48:24', 1),
(1282, '2018-04-14', '23281962-Joseph Tolah has logged out at 00:48:49', 1),
(1283, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:50:18', 1),
(1284, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:54:18', 1),
(1285, '2018-04-14', '23281962-Joseph Tolah has logged out at 00:54:59', 1),
(1286, '2018-04-14', '23281962-Joseph Tolah is logged in at 00:55:12', 1),
(1287, '2018-04-14', '23281962-Joseph Tolah has logged out at 00:56:11', 1),
(1288, '2018-04-14', '23281962-Joseph Tolah is logged in at 01:16:56', 1),
(1289, '2018-04-14', '23281962-Joseph Tolah is logged in at 07:25:54', 1),
(1290, '2018-04-14', '23281962-Joseph Tolah is logged in at 07:26:23', 1),
(1291, '2018-04-14', '23281962-Joseph Tolah has logged out at 07:26:33', 1),
(1292, '2018-04-14', '23281962-Joseph Tolah is logged in at 08:51:08', 1),
(1293, '2018-04-14', 'null-null has logged out at 08:58:12', 1),
(1294, '2018-04-14', '23281962-Joseph Tolah Updated Client RIKENS SUPERMARKET+\"\' && DELETE FROM USERSTABLE\"; ON 2018-04-14 AT 08:59:01', 1),
(1295, '2018-04-14', '23281962-Joseph Tolah Updated Client +\"\' && DELETE FROM USERSTABLE\";// ON 2018-04-14 AT 08:59:39', 1),
(1296, '2018-04-14', '23281962-Joseph Tolah is logged in at 09:01:08', 1),
(1297, '2018-04-14', '23281962-Joseph Tolah Updated Client +\"\' || DELETE FROM USERSTABLE\";// ON 2018-04-14 AT 09:01:33', 1),
(1298, '2018-04-14', '23281962-Joseph Tolah has logged out at 09:02:04', 1),
(1299, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:18:23', 1),
(1300, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:19:18', 1),
(1301, '2018-04-14', '23281962-Joseph Tolah has logged out at 10:19:26', 1),
(1302, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:53:15', 1),
(1303, '2018-04-14', '23281962-Joseph Tolah has logged out at 10:53:23', 1),
(1304, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:53:56', 1),
(1305, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:54:47', 1),
(1306, '2018-04-14', '23281962-Joseph Tolah is logged in at 10:58:59', 1),
(1307, '2018-04-14', '23281962-Joseph Tolah has logged out at 10:59:09', 1),
(1308, '2018-04-14', 'null-null has logged out at 12:56:17', 1),
(1309, '2018-04-14', '23281962-Joseph Tolah is logged in at 12:57:06', 1),
(1310, '2018-04-14', '23281962-Joseph Tolah is logged in at 13:00:07', 1),
(1311, '2018-04-14', '23281962-Joseph Tolah is logged in at 13:00:40', 1),
(1312, '2018-04-14', '23281962-Joseph Tolah is logged in at 13:21:19', 1),
(1313, '2018-04-14', '23281962-Joseph Tolah is logged in at 13:25:54', 1),
(1314, '2018-04-14', '23281962-Joseph Tolah has logged out at 13:26:17', 1),
(1315, '2018-04-14', '23281962-Joseph Tolah has logged out at 13:26:22', 1),
(1316, '2018-04-14', '23281962-Joseph Tolah has logged out at 13:26:27', 1),
(1317, '2018-04-14', '23281962-Joseph Tolah is logged in at 13:27:44', 1),
(1318, '2018-04-14', '23281962-Joseph Tolah has logged out at 13:29:08', 1),
(1319, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:07:41', 1),
(1320, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:07:59', 1),
(1321, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:10:13', 1),
(1322, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:10:21', 1),
(1323, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:11:01', 1),
(1324, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:11:07', 1),
(1325, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:16:37', 1),
(1326, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:17:28', 1),
(1327, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:17:37', 1),
(1328, '2018-04-14', 'null-null has logged out at 16:17:50', 1),
(1329, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:18:05', 1),
(1330, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:18:15', 1),
(1331, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:19:38', 1),
(1332, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:19:55', 1),
(1333, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:20:27', 1),
(1334, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:20:39', 1),
(1335, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:23:39', 1),
(1336, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:23:54', 1),
(1337, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:24:31', 1),
(1338, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:24:44', 1),
(1339, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:25:45', 1),
(1340, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:25:53', 1),
(1341, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:26:27', 1),
(1342, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:26:45', 1),
(1343, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:28:38', 1),
(1344, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:29:02', 1),
(1345, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:29:44', 1),
(1346, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:29:57', 1),
(1347, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:37:57', 1),
(1348, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:39:56', 1),
(1349, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:40:07', 1),
(1350, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:43:16', 1),
(1351, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:45:20', 1),
(1352, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:46:53', 1),
(1353, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:47:50', 1),
(1354, '2018-04-14', '23281962-Joseph Tolah is logged in at 16:47:57', 1),
(1355, '2018-04-14', '23281962-Joseph Tolah has logged out at 16:48:35', 1),
(1356, '2018-04-14', '23281962-Joseph Tolah is logged in at 21:04:25', 1),
(1357, '2018-04-14', '23281962-Joseph Tolah has logged out at 21:06:20', 1),
(1358, '2018-04-15', '23281962-Joseph Tolah is logged in at 16:18:51', 1),
(1359, '2018-04-15', '23281962-Joseph Tolah has logged out at 16:22:11', 1),
(1360, '2018-04-15', '23281962-Joseph Tolah is logged in at 16:29:08', 1),
(1361, '2018-04-15', '23281962-Joseph Tolah has logged out at 16:31:06', 1),
(1362, '2018-04-15', '23281962-Joseph Tolah is logged in at 16:47:25', 1),
(1363, '2018-04-15', '23281962-Joseph Tolah has logged out at 16:47:57', 1),
(1364, '2018-04-15', 'null-null has logged out at 17:06:08', 1),
(1365, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:13:16', 1),
(1366, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:13:44', 1),
(1367, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:14:47', 1),
(1368, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:15:13', 1),
(1369, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:15:29', 1),
(1370, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:15:49', 1),
(1371, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:22:10', 1),
(1372, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:22:32', 1),
(1373, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:23:01', 1),
(1374, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:23:38', 1),
(1375, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:24:05', 1),
(1376, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:24:42', 1),
(1377, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:26:20', 1),
(1378, '2018-04-15', '23281962-Joseph Tolah has logged out at 17:26:48', 1),
(1379, '2018-04-15', '23281962-Joseph Tolah is logged in at 17:35:51', 1),
(1380, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:28:31', 1),
(1381, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:30:25', 1),
(1382, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:31:36', 1),
(1383, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:38:04', 1),
(1384, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:38:21', 1),
(1385, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:41:11', 1),
(1386, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:42:07', 1),
(1387, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:42:36', 1),
(1388, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:44:37', 1),
(1389, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:44:49', 1),
(1390, '2018-04-15', 'null-null has logged out at 19:45:44', 1),
(1391, '2018-04-15', 'null-null has logged out at 19:50:06', 1),
(1392, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:50:20', 1),
(1393, '2018-04-15', '23281962-Joseph Tolah has logged out at 19:51:07', 1),
(1394, '2018-04-15', '23281962-Joseph Tolah is logged in at 19:53:40', 1),
(1395, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:00:55', 1),
(1396, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:01:56', 1),
(1397, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:02:04', 1),
(1398, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:09:02', 1),
(1399, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:10:21', 1),
(1400, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:11:38', 1),
(1401, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:11:51', 1),
(1402, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:12:03', 1),
(1403, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:12:15', 1),
(1404, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:33:46', 1),
(1405, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:33:56', 1),
(1406, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:34:25', 1),
(1407, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:34:33', 1),
(1408, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:35:19', 1),
(1409, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:35:25', 1),
(1410, '2018-04-15', 'null-null has logged out at 20:35:45', 1),
(1411, '2018-04-15', 'null-null has logged out at 20:35:57', 1),
(1412, '2018-04-15', 'null-null has logged out at 20:43:12', 1),
(1413, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:46:28', 1),
(1414, '2018-04-15', '23281962-Joseph Tolah has logged out at 20:46:51', 1),
(1415, '2018-04-15', 'null-null has logged out at 20:50:29', 1),
(1416, '2018-04-15', 'null-null has logged out at 20:54:14', 1),
(1417, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:56:51', 1),
(1418, '2018-04-15', '23281962-Joseph Tolah is logged in at 20:57:57', 1),
(1419, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:02:35', 1),
(1420, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:02:42', 1),
(1421, '2018-04-15', 'null-null has logged out at 21:03:59', 1),
(1422, '2018-04-15', 'null-null has logged out at 21:04:05', 1),
(1423, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:04:12', 1),
(1424, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:05:07', 1),
(1425, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:05:16', 1),
(1426, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:05:21', 1),
(1427, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:05:51', 1),
(1428, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:05:59', 1),
(1429, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:07:27', 1),
(1430, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:07:33', 1),
(1431, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:07:47', 1),
(1432, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:07:56', 1),
(1433, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:08:35', 1),
(1434, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:08:42', 1),
(1435, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:10:39', 1),
(1436, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:10:55', 1),
(1437, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:11:13', 1),
(1438, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:11:45', 1),
(1439, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:12:00', 1),
(1440, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:12:06', 1),
(1441, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:13:05', 1),
(1442, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:13:20', 1),
(1443, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:13:37', 1),
(1444, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:13:43', 1),
(1445, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:14:36', 1),
(1446, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:14:41', 1),
(1447, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:15:17', 1),
(1448, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:15:36', 1),
(1449, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:15:52', 1),
(1450, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:15:58', 1),
(1451, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:16:26', 1),
(1452, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:19:40', 1),
(1453, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:30:14', 1),
(1454, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:30:27', 1),
(1455, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:30:54', 1),
(1456, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:32:39', 1),
(1457, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:37:04', 1),
(1458, '2018-04-15', '23281962-Joseph Tolah Updated \' ON 2018-04-15 AT 21:37:36', 1),
(1459, '2018-04-15', '23281962-Joseph Tolah Updated User \' ON 2018-04-15 AT 21:37:52', 1),
(1460, '2018-04-15', '23281962-Joseph Tolah Updated \' ON 2018-04-15 AT 21:37:52', 1),
(1461, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:38:08', 1),
(1462, '2018-04-15', '23281962-Joseph Tolah is logged in at 21:38:36', 1),
(1463, '2018-04-15', '23281962-Joseph Tolah Updated User \' ON 2018-04-15 AT 21:38:48', 1),
(1464, '2018-04-15', '23281962-Joseph Tolah Updated \' ON 2018-04-15 AT 21:38:48', 1),
(1465, '2018-04-15', '23281962-Joseph Tolah has logged out at 21:39:07', 1),
(1466, '2018-04-16', '23281962-Joseph Tolah is logged in at 10:53:07', 1),
(1467, '2018-04-19', '23281962-Joseph Tolah is logged in at 23:36:03', 1),
(1468, '2018-04-19', '23281962-Joseph Tolah has logged out at 23:36:08', 1),
(1469, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:17:21', 1),
(1470, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:17:35', 1),
(1471, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:30:18', 1),
(1472, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:30:23', 1),
(1473, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:31:05', 1),
(1474, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:31:07', 1),
(1475, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:31:31', 1),
(1476, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:31:38', 1),
(1477, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:32:00', 1),
(1478, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:32:12', 1),
(1479, '2018-04-20', 'null-null has logged out at 00:32:32', 1),
(1480, '2018-04-20', 'null-null has logged out at 00:33:08', 1),
(1481, '2018-04-20', 'null-null has logged out at 00:33:26', 1),
(1482, '2018-04-20', 'null-null has logged out at 00:33:47', 1),
(1483, '2018-04-20', 'null-null has logged out at 00:34:17', 1),
(1484, '2018-04-20', 'null-null has logged out at 00:34:43', 1),
(1485, '2018-04-20', 'null-null has logged out at 00:34:57', 1),
(1486, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:35:09', 1),
(1487, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:35:24', 1),
(1488, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:36:32', 1),
(1489, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:36:41', 1),
(1490, '2018-04-20', '23281962-Joseph Tolah is logged in at 00:37:01', 1),
(1491, '2018-04-20', '23281962-Joseph Tolah has logged out at 00:37:13', 1),
(1492, '2018-04-20', '23281962-Joseph Tolah is logged in at 01:27:33', 1),
(1493, '2018-04-20', '23281962-Joseph Tolah is logged in at 08:55:07', 1),
(1494, '2018-04-20', '23281962-Joseph Tolah is logged in at 09:12:53', 1),
(1495, '2018-04-20', 'null-null has logged out at 09:15:06', 1),
(1496, '2018-04-20', 'null-null has logged out at 09:29:05', 1),
(1497, '2018-04-20', '23281962-Joseph Tolah is logged in at 10:28:33', 1),
(1498, '2018-04-20', '23281962-Joseph Tolah is logged in at 10:37:03', 1),
(1499, '2018-04-20', '23281962-Joseph Tolah has logged out at 10:37:45', 1),
(1500, '2018-04-20', '23281962-Joseph Tolah has logged out at 10:37:57', 1),
(1501, '2018-04-20', '23281962-Joseph Tolah is logged in at 10:39:41', 1),
(1502, '2018-04-20', '23281962-Joseph Tolah has logged out at 10:40:04', 1),
(1503, '2018-04-20', '23281962-Joseph Tolah is logged in at 13:04:05', 1),
(1504, '2018-04-20', '23281962-Joseph Tolah is logged in at 22:10:13', 1),
(1505, '2018-04-20', '23281962-Joseph Tolah is logged in at 22:20:37', 1);

-- --------------------------------------------------------

--
-- Table structure for table `paymentstable`
--

CREATE TABLE `paymentstable` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `details` varchar(255) NOT NULL,
  `payment_date` date NOT NULL,
  `debit` double NOT NULL,
  `credit` double NOT NULL,
  `expenses_amount` double NOT NULL,
  `s` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paymentstable`
--

INSERT INTO `paymentstable` (`id`, `client_id`, `details`, `payment_date`, `debit`, `credit`, `expenses_amount`, `s`, `user_id`) VALUES
(79, 6, 'INV-180412170635', '2018-04-12', 0, 9000, 0, 0, 1),
(80, 6, 'INV-180412174624', '2018-04-12', 0, 89000, 0, 0, 1),
(81, 8, 'INV-180412174702', '2018-04-12', 0, 64900, 0, 0, 1),
(82, 6, 'INV-180412175630', '2018-04-12', 0, 177900, 0, 1, 1),
(83, 8, 'INV-180412180209', '2018-04-12', 0, 80000, 0, 0, 1),
(84, 1, 'EXP-180412182945', '2018-04-12', 0, 0, 9000, 0, 1),
(85, 6, 'INV-180413090454', '2018-04-13', 0, 15000, 0, 1, 1),
(86, 9, 'PYMT-180413090832', '2018-04-13', 8000, 0, 0, 1, 1),
(87, 9, 'INV-180413090945', '2018-04-13', 0, 9000, 0, 1, 1),
(88, 1, 'EXP-180413091042', '2018-04-13', 0, 0, 1300, 1, 1),
(89, 6, 'PYMT-180413094441', '2018-04-13', 9000, 0, 0, 1, 1),
(90, 6, 'INV-180413125337', '2018-04-13', 0, 9000, 0, 1, 1),
(91, 6, 'INV-180414210431', '2018-04-14', 0, 80900, 0, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `servicetable`
--

CREATE TABLE `servicetable` (
  `id` int(11) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `servicetable`
--

INSERT INTO `servicetable` (`id`, `service_name`, `s`) VALUES
(8, 'SECURITY GUARD', 1),
(9, 'ALARMS', 1),
(10, 'CCTV INSTALLATION AND MAINTENANCE', 1),
(11, 'CLEANING', 1);

-- --------------------------------------------------------

--
-- Table structure for table `userstable`
--

CREATE TABLE `userstable` (
  `id` int(11) NOT NULL,
  `id_number` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `last_login` varchar(255) NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL,
  `logged` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userstable`
--

INSERT INTO `userstable` (`id`, `id_number`, `name`, `phone_no`, `user_type`, `username`, `password`, `last_login`, `company_id`, `s`, `logged`) VALUES
(1, '23281962', 'Joseph Tolah', '0723095840', 'Admin', 'admin', 'admin', '2018-04-20 22:20:36', 1, 1, 1),
(11, '234345234', 'EMILY KUBO', '0723090850', 'Admin', 'admin', 'admin', '0000-00-00 00:00:00', 1, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `backup_table`
--
ALTER TABLE `backup_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Clientstable`
--
ALTER TABLE `Clientstable`
  ADD PRIMARY KEY (`clientid`);

--
-- Indexes for table `clients_paytable`
--
ALTER TABLE `clients_paytable`
  ADD PRIMARY KEY (`payment_id`);

--
-- Indexes for table `companytable`
--
ALTER TABLE `companytable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `expensestable`
--
ALTER TABLE `expensestable`
  ADD PRIMARY KEY (`expense_id`);

--
-- Indexes for table `invoiceinfo`
--
ALTER TABLE `invoiceinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `logtable`
--
ALTER TABLE `logtable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `paymentstable`
--
ALTER TABLE `paymentstable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `servicetable`
--
ALTER TABLE `servicetable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `userstable`
--
ALTER TABLE `userstable`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `backup_table`
--
ALTER TABLE `backup_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `Clientstable`
--
ALTER TABLE `Clientstable`
  MODIFY `clientid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `companytable`
--
ALTER TABLE `companytable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `invoiceinfo`
--
ALTER TABLE `invoiceinfo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT for table `logtable`
--
ALTER TABLE `logtable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1506;

--
-- AUTO_INCREMENT for table `paymentstable`
--
ALTER TABLE `paymentstable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=92;

--
-- AUTO_INCREMENT for table `servicetable`
--
ALTER TABLE `servicetable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `userstable`
--
ALTER TABLE `userstable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
